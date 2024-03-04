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

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@Scope(value = "prototype")
public class SheffJobCreateOrder implements Runnable {
    private final IOrderRepository orderRepository;
    private final DishOrderConnector dishOrderConnector;
    private final IUnitOfWork unitOfWork;
    private final List<Dish> dishes;
    private final String transactionId;

    @Override
    public void run() {
        System.out.println("worker started");
        unitOfWork.startTransaction();

        long finishedAt = 1;

        for (var dish : dishes) {
            finishedAt = Math.max(finishedAt, dish.getDurationSeconds());
        }
        LocalDateTime currentTime = LocalDateTime.now().plusSeconds(finishedAt);

        System.out.println("time to finish: " + currentTime + " seconds: " + finishedAt);

        var orderId = orderRepository
                .add(Order.builder()
                        .transactionId(transactionId)
                        .finished_at(Timestamp.valueOf(currentTime))
                        .build());

        System.out.println("Job was started by sheff, transaction=" + transactionId + " order_id: " + orderId);
        if (orderId == null) {
            unitOfWork.rollback();
            return;
        }

        for (var dish : dishes) {
            dishOrderConnector.createConnectedRow(dish.getId(), orderId, dish.getAmount());
        }

        System.out.println("Job was hardworking by sheff, transaction=" + transactionId + " order_id: " + orderId);
        try {
            Thread.sleep(finishedAt * 1000);
        } catch (Exception ignored) {
            unitOfWork.rollback();
            return;
        }

        unitOfWork.commit();
        System.out.println("worker end");
        System.out.println("Sheff was done his job, order_id = " + orderId);
    }
}
