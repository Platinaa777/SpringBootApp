package restaurant.coredomain.application.jobs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import restaurant.coredomain.domain.entities.Dish;
import restaurant.coredomain.domain.entities.Order;
import restaurant.coredomain.domain.entities.enums.OrderType;
import restaurant.coredomain.domain.repositories.IOrderRepository;
import restaurant.coredomain.domain.repositories.IUnitOfWork;
import restaurant.infrastructure.dbrelationships.DishOrderConnector;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Scope(value = "prototype")
public class SheffJobAppend implements Runnable {
    private final IOrderRepository orderRepository;
    private final DishOrderConnector dishOrderConnector;
    private final IUnitOfWork unitOfWork;
    private final Dish dish;
    private final String transactionId;
    private final Long orderId;

    @Override
    public void run() {
        unitOfWork.startTransaction();

        Long duration = dish.getDurationSeconds();
        var shouldBeUpdate = orderRepository.updateOrderFinishedTime(orderId, duration);
        dishOrderConnector.createConnectedRow(dish.getId(), orderId, dish.getAmount());

        try {
            Thread.sleep(duration * 1000);
        } catch (Exception ignored) {
            unitOfWork.rollback();
            return;
        }

        unitOfWork.commit();
        System.out.println("Sheff was done his job, order_id = " + orderId);
    }
}
