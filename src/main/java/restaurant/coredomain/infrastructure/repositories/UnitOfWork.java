package restaurant.coredomain.infrastructure.repositories;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import restaurant.coredomain.domain.entities.Dish;
import restaurant.coredomain.domain.repositories.IUnitOfWork;
import restaurant.infrastructure.connfactories.SqlDishStatements;
import restaurant.infrastructure.interfaces.IDbConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

@Component
@Scope(value = "prototype")
public class UnitOfWork implements IUnitOfWork {

    private final IDbConnectionFactory<Connection> factory;

    public UnitOfWork(IDbConnectionFactory<Connection> factory) {

        this.factory = factory;
    }

    @Override
    public void startTransaction() {
        var connection = factory.getConnection();

        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollback() {
        var connection = factory.getConnection();
        try {
            connection.rollback();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit() {
        var connection = factory.getConnection();
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        var connection = factory.getConnection();
        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
