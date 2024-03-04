package restaurant.coredomain.application.services;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import restaurant.coredomain.application.commands.sheff.SheffOrder;
import restaurant.coredomain.application.jobs.SheffJobAppend;
import restaurant.coredomain.application.jobs.SheffJobCreateOrder;
import restaurant.coredomain.domain.entities.Dish;
import restaurant.coredomain.infrastructure.repositories.OrderRepository;
import restaurant.coredomain.infrastructure.repositories.UnitOfWork;
import restaurant.infrastructure.connfactories.NpgsqlConnectionFactory;
import restaurant.infrastructure.dbrelationships.DishOrderConnector;

import javax.sql.DataSource;

@Service
@AllArgsConstructor
@Scope(value = "singleton")
public class SheffService {
    private DataSource dataSource;

    // should launch the thread with some job
    public void acceptOrderToCook(SheffOrder task) {
        var workingThread = new Thread(
                SheffJobCreateOrder.builder()
                .dishOrderConnector(new DishOrderConnector(new NpgsqlConnectionFactory(dataSource)))
                .orderRepository(new OrderRepository(new NpgsqlConnectionFactory(dataSource)))
                .unitOfWork(new UnitOfWork(new NpgsqlConnectionFactory(dataSource)))
                .dishes(task.getDishes())
                .transactionId(task.getTransactionId())
                .build());

        workingThread.start();
    }


    public void appendDishToOrder(Dish dish, Long orderId, String transactionId) {
        var workingThread = new Thread(
                SheffJobAppend.builder()
                        .dishOrderConnector(new DishOrderConnector(new NpgsqlConnectionFactory(dataSource)))
                        .orderRepository(new OrderRepository(new NpgsqlConnectionFactory(dataSource)))
                        .unitOfWork(new UnitOfWork(new NpgsqlConnectionFactory(dataSource)))
                        .dish(dish)
                        .orderId(orderId)
                        .transactionId(transactionId)
                        .build());

        workingThread.start();

        System.out.println("Append Job was accepted by sheff, transaction=" + transactionId);
    }
}
