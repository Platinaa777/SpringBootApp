package restaurant.infrastructure.connfactories;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import restaurant.infrastructure.interfaces.IDbConnectionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@Scope(value = "prototype")
public class NpgsqlConnectionFactory implements IDbConnectionFactory<Connection> {
    private final DataSource source;
    private Connection connection;

    public NpgsqlConnectionFactory(DataSource source) {
        this.source = source;
    }

    @Override
    public Connection getConnection(){
        if (connection != null) {
            return connection;
        }

        try {
            connection = source.getConnection();
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void closeConnection() {
        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
