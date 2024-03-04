package restaurant.coredomain.infrastructure.repositories;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import restaurant.coredomain.domain.entities.Dish;
import restaurant.coredomain.domain.entities.Order;
import restaurant.coredomain.domain.repositories.IOrderRepository;
import restaurant.infrastructure.connfactories.NpgsqlConnectionFactory;
import restaurant.infrastructure.connfactories.SqlDishStatements;
import restaurant.infrastructure.connfactories.SqlOrderStatements;
import restaurant.infrastructure.interfaces.IDbConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
@Scope(value = "prototype")
public class OrderRepository implements IOrderRepository {
   private final IDbConnectionFactory<Connection> factory;

    public OrderRepository(IDbConnectionFactory<Connection> factory) {
        this.factory = factory;
    }

    @Override
    public Long add(Order order) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlOrderStatements.sqlAddOrder);

            sqlStatement.setString(1, order.getTransactionId());
            sqlStatement.setTimestamp(2, order.getFinished_at());

            var result = sqlStatement.executeQuery();
            result.next();
            var id = result.getLong("id");

            // one row was affected
            factory.closeConnection();
            return id != 0 ? id : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        factory.closeConnection();
        return null;
    }

    @Override
    public boolean remove(Long id)
    {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlOrderStatements.sqlRemoveOrder);

            sqlStatement.setLong(1, id);

            var result = sqlStatement.executeUpdate();

            factory.closeConnection();
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        factory.closeConnection();
        return false;
    }

    @Override
    public Order findOrder(String transactionId)
    {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlOrderStatements.sqlFindOrdersOwnToClient);

            sqlStatement.setString(1, transactionId);

            var result = sqlStatement.executeQuery();

            int i = 0;
            Order order = null;
            while (result.next()) {
                if (i == 0) {
                    order = Order.builder()
                            .id(result.getLong("order_id"))
                            .transactionId(result.getString("transaction_id"))
                            .started_at(result.getTimestamp("started_at"))
                            .finished_at(result.getTimestamp("finished_at"))
                            .build();
                    var dish = Dish.builder()
                            .id(result.getLong("dish_id"))
                            .title(result.getString("title"))
                            .price(result.getLong("price"))
                            .amount(result.getLong("dish_amount"))
                            .durationSeconds(result.getLong("duration_seconds"))
                            .build();
                    order.setDishes(new ArrayList<>());
                    order.getDishes().add(dish);
                } else {
                    var dish = Dish.builder()
                            .id(result.getLong("dish_id"))
                            .title(result.getString("title"))
                            .price(result.getLong("price"))
                            .amount(result.getLong("dish_amount"))
                            .durationSeconds(result.getLong("duration_seconds"))
                            .build();

                    order.getDishes().add(dish);
                }
                ++i;
            }

            factory.closeConnection();
            return order;
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        factory.closeConnection();
        return null;
    }


    @Override
    public boolean updateOrderFinishedTime(Long id, Long duration) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlOrderStatements.sqlUpdateOrder);

            LocalDateTime currentTime = LocalDateTime.now().plusSeconds(duration);
            Timestamp timestamp = Timestamp.valueOf(currentTime);
            sqlStatement.setTimestamp(1, timestamp);
            sqlStatement.setLong(2, id);
            sqlStatement.setTimestamp(3, timestamp);


            var result = sqlStatement.executeUpdate();
            factory.closeConnection();
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        factory.closeConnection();
        return false;
    }
}
