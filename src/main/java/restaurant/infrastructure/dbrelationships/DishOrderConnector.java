package restaurant.infrastructure.dbrelationships;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import restaurant.infrastructure.connfactories.SqlOrderStatements;
import restaurant.infrastructure.interfaces.IDbConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

@Component
@Scope(value = "prototype")
public class DishOrderConnector {
    private final IDbConnectionFactory<Connection> factory;

    public DishOrderConnector(IDbConnectionFactory<Connection> factory) {
        this.factory = factory;
    }

    public boolean createConnectedRow(Long dishId, Long orderId, Long dishAmount) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(addRow);

            sqlStatement.setLong(1, dishId);
            sqlStatement.setLong(2, orderId);
            sqlStatement.setLong(3, dishAmount);

            var result = sqlStatement.executeUpdate();

            // one row was affected
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String addRow = "INSERT INTO dish_order (dish_id, order_id, dish_amount) VALUES (?,?,?);";
}
