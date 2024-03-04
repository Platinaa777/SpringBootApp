package restaurant.coredomain.infrastructure.repositories;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import restaurant.coredomain.domain.entities.Dish;
import restaurant.coredomain.domain.entities.Transaction;
import restaurant.coredomain.domain.repositories.ITransactionRepository;
import restaurant.infrastructure.connfactories.NpgsqlConnectionFactory;
import restaurant.infrastructure.connfactories.SqlDishStatements;
import restaurant.infrastructure.connfactories.SqlTransactionStatements;
import restaurant.infrastructure.interfaces.IDbConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Scope(value = "prototype")
public class TransactionRepository implements ITransactionRepository {

    private final IDbConnectionFactory<Connection> factory;

    public TransactionRepository(IDbConnectionFactory<Connection> factory) {
        this.factory = factory;
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String create(String email) {
        var connection = factory.getConnection();

        try {
            var id = generateUUID();

            var sqlStatement = connection.prepareStatement(SqlTransactionStatements.sqlCreateTransaction);

            sqlStatement.setString(1, id);
            sqlStatement.setString(2, email);

            var result = sqlStatement.executeUpdate();

            factory.closeConnection();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        factory.closeConnection();
        return null;
    }

    @Override
    public boolean update(String id, Long totalEarning) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlTransactionStatements.sqlCloseTransaction);
            sqlStatement.setLong(1, totalEarning);
            sqlStatement.setString(2, id);
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
    public Transaction find(String id) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlTransactionStatements.sqlFindTransaction);

            sqlStatement.setString(1, id);

            var result = sqlStatement.executeQuery();
            result.next();

            var transaction = Transaction.builder()
                    .id(result.getString("id"))
                    .clientEmail(result.getString("client_email"))
                    .earnings(result.getLong("earnings"))
                    .status(result.getString("status"))
                    .build();

            factory.closeConnection();
            return transaction;
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        factory.closeConnection();
        return null;
    }

    @Override
    public List<Transaction> findAll() {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement("SELECT * FROM transactions");
            var result = sqlStatement.executeQuery();
            List<Transaction> transactions = new ArrayList<>();
            while (result.next()) {
                var transaction = Transaction.builder()
                        .id(result.getString("id"))
                        .clientEmail(result.getString("client_email"))
                        .earnings(result.getLong("earnings"))
                        .status(result.getString("status"))
                        .build();
                transactions.add(transaction);
            }

            factory.closeConnection();
            return transactions;
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        factory.closeConnection();
        return null;
    }
}
