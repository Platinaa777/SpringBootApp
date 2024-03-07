package restaurant.auth.repositories.implementations;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import restaurant.auth.models.Client;
import restaurant.auth.repositories.SqlAuthStatements;
import restaurant.auth.repositories.interfaces.IUserRepository;
import restaurant.coredomain.domain.entities.Dish;
import restaurant.infrastructure.connfactories.SqlDishStatements;
import restaurant.infrastructure.interfaces.IDbConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "prototype")
public class UserRepository implements IUserRepository {
    private final IDbConnectionFactory<Connection> factory;

    public UserRepository(IDbConnectionFactory<Connection> factory) {
        this.factory = factory;
    }

    @Override
    public boolean create(Client user) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlAuthStatements.sqlCreateUser);

            sqlStatement.setString(1, user.getEmail());
            sqlStatement.setString(2, user.getPassword());
            sqlStatement.setString(3, user.getRole());


            var result = sqlStatement.executeUpdate();

            factory.closeConnection();
            return result == 1;
        } catch (SQLException e) {
//             e.printStackTrace();
        }
        factory.closeConnection();
        return false;
    }

    @Override
    public boolean isExist(Client user) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlAuthStatements.sqlUserExist);

            sqlStatement.setString(1, user.getEmail());

            var result = sqlStatement.executeQuery();
            result.next();

            var client = Client.builder()
                    .id(result.getLong("id"))
                    .build();
            factory.closeConnection();
            return client.getId() != null;
        } catch (SQLException e) {
             // e.printStackTrace();
        }
        factory.closeConnection();
        return false;
    }

    @Override
    public Client find(String email) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlAuthStatements.sqlUserSelect);

            sqlStatement.setString(1, email);

            var result = sqlStatement.executeQuery();
            result.next();

            var client = Client.builder()
                    .id(result.getLong("id"))
                    .email(result.getString("email"))
                    .password(result.getString("password"))
                    .role(result.getString("role"))
                    .build();
            factory.closeConnection();

            return client;
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        factory.closeConnection();
        return null;
    }

    @Override
    public List<Client> getAll() {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement("SELECT * FROM clients");

            var result = sqlStatement.executeQuery();
            List<Client> clients = new ArrayList<>();
            while (result.next()) {
                var client = Client.builder()
                        .id(result.getLong("id"))
                        .email(result.getString("email"))
                        .password(result.getString("password"))
                        .role(result.getString("role"))
                        .build();

                clients.add(client);
            }


            factory.closeConnection();

            return clients;
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        factory.closeConnection();
        return null;
    }
}
