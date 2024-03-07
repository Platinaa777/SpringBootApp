package restaurant.coredomain.application.services;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import restaurant.coredomain.application.commands.order.AppendDishOrder;
import restaurant.coredomain.application.commands.order.CreateOrder;
import restaurant.coredomain.application.commands.sheff.SheffOrder;
import restaurant.coredomain.application.responses.ResponseType;
import restaurant.coredomain.application.responses.status.DishStatusResponse;
import restaurant.coredomain.application.responses.status.OrderStatusResponse;
import restaurant.coredomain.domain.entities.Dish;
import restaurant.coredomain.domain.entities.enums.TransactionType;
import restaurant.coredomain.domain.repositories.IDishRepository;
import restaurant.coredomain.domain.repositories.IOrderRepository;
import restaurant.coredomain.domain.repositories.ITransactionRepository;
import restaurant.coredomain.domain.repositories.IUnitOfWork;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Scope(value = "prototype")
public class OrderHandler {

    private final ITransactionRepository transactionRepository;
    private final IOrderRepository orderRepository;
    private final IDishRepository dishRepository;
    private final IUnitOfWork unitOfWork;
    private final SheffService sheffService;

    public ResponseType createOrder(CreateOrder order) {
        unitOfWork.startTransaction();
        var transactionId = transactionRepository.create(order.getClientEmail());

        // was thrown exception
        if (transactionId == null) {
            unitOfWork.rollback();
            return new ResponseType(ResponseType.ErrorWithTransaction, false);
        }

        // find all dishes in stock
        List<Dish> dishes = new ArrayList<>();
        for (var clientOrder : order.getDishes()) {
            var dish = dishRepository.findByTitle(clientOrder.getTitle());

            // we can not find one dish in order => cancel the order
            if (dish == null) {
                unitOfWork.rollback();
                return new ResponseType(ResponseType.DishNotFoundWithName(clientOrder.getTitle()), false);
            }

            // have less amount that required
            if (dish.getAmount() < clientOrder.getAmount()) {
                unitOfWork.rollback();
                return new ResponseType(ResponseType.AmountError(clientOrder.getTitle()), false);
            }

            dish.setAmount(dish.getAmount() - clientOrder.getAmount());
            // reserve our amount dish for sheff
            // if null: cant update dish amount because another thread changed the amount in database level
            if (dishRepository.update(dish) == null) {
                unitOfWork.rollback();
                return new ResponseType(ResponseType.ErrorWhileUpdatingAmount(clientOrder.getTitle()), false);
            }

            dish.setAmount(clientOrder.getAmount());
            dishes.add(dish);
        }

        sheffService.acceptOrderToCook(
                SheffOrder.builder()
                .dishes(dishes)
                .transactionId(transactionId)
                .build());

        unitOfWork.commit();

        return new ResponseType(ResponseType.OrderCreated + "with id = " + transactionId, true);
    }

    public boolean cancelOrder(String transactionId) {
        var order = orderRepository.findOrder(transactionId);

        if (order == null || order.getFinished_at().before(new Date()))
            return false;

        for (var item : order.getDishes()) {
            var storedDish = dishRepository.findByTitle(item.getTitle());
            storedDish.setAmount(storedDish.getAmount() + item.getAmount());
            dishRepository.update(storedDish);
        }

        return orderRepository.remove(order.getId());
    }

    public ResponseType appendDishToOrder(AppendDishOrder order) {
        // find transaction
        unitOfWork.startTransaction();

        var transaction = transactionRepository.find(order.getTransactionId());
        if (transaction != null && transaction.getStatus().equals(TransactionType.FINISHED))
            return new ResponseType(ResponseType.ErrorWithTransaction, false);

        // find dish
        var dish = dishRepository.findByTitle(order.getTitle());
        if (dish == null)
            return new ResponseType(ResponseType.DishNotFoundWithName(order.getTitle()), false);

        dish.setAmount(dish.getAmount() - order.getAmount());
        if (dishRepository.update(dish) == null) {
            unitOfWork.rollback();
            return new ResponseType(ResponseType.AmountError(order.getTitle()), false);
        }
        dish.setAmount(order.getAmount());

        // find exist order
        var foundOrder = orderRepository.findOrder(order.getTransactionId());
        if (foundOrder == null)
            return new ResponseType(ResponseType.OrderDoesNotExist(order.getEmail()), false);

        if (foundOrder.getFinished_at().before(new Date()))
            return new ResponseType(ResponseType.ErrorAppendOrder, false);

        sheffService.appendDishToOrder(dish, foundOrder.getId(), order.getTransactionId());

        unitOfWork.commit();
        return new ResponseType(ResponseType.OrderAppended, true);
    }


    public OrderStatusResponse findOrder(String transactionId) {
        var order = orderRepository.findOrder(transactionId);

        if (order == null)
            return null;

        OrderStatusResponse orderStatusResponse = OrderStatusResponse.builder()
                .finished_at(order.getFinished_at())
                .started_at(order.getStarted_at())
                .build();

        List<DishStatusResponse> dishes = new ArrayList<>();
        for (var item : order.getDishes()) {
            var dishView = DishStatusResponse.builder()
                    .title(item.getTitle())
                    .price(item.getPrice())
                    .amount(item.getAmount())
                    .build();

            dishes.add(dishView);
        }
        orderStatusResponse.setDishes(dishes);

        if (order.getFinished_at().before(new Date())) {
            orderStatusResponse.setReady(true);
        } else {
            orderStatusResponse.setReady(false);
        }

        return orderStatusResponse;
    }

    public ResponseType pay(String transactionId) {
        var order = orderRepository.findOrder(transactionId);

        if (order == null)
            return new ResponseType(ResponseType.OrderCantBePaid, false);

        if (!order.getFinished_at().before(new Date()))
            return new ResponseType(ResponseType.OrderCantBePaid, false);

        long earnings = 0L;
        for (var item : order.getDishes()) {
            earnings += item.getPrice() * item.getAmount();
        }

        var response = transactionRepository.update(transactionId, earnings);

        if (response)
            return new ResponseType(ResponseType.OrderWasSuccessfullyPaid, true);
        else
            return new ResponseType(ResponseType.OrderCantBePaid, false);
    }
}
