package restaurant.coredomain.infrastructure.repositories;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import restaurant.infrastructure.connfactories.NpgsqlConnectionFactory;
import restaurant.infrastructure.connfactories.SqlDishStatements;
import restaurant.coredomain.domain.entities.Dish;
import restaurant.coredomain.domain.repositories.IDishRepository;
import restaurant.infrastructure.interfaces.IDbConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "prototype")
public class DishRepository implements IDishRepository {

    private final IDbConnectionFactory<Connection> factory;

    public DishRepository(IDbConnectionFactory<Connection> factory) {

        this.factory = factory;
    }

    @Override
    public Dish findByTitle(String title) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlDishStatements.dishFindByTitle);

            sqlStatement.setString(1, title);

            var result = sqlStatement.executeQuery();
            result.next();

            var dish = Dish.builder()
                    .id(result.getLong("id"))
                    .title(result.getString("title"))
                    .amount(result.getLong("amount"))
                    .price(result.getLong("price"))
                    .durationSeconds(result.getLong("duration_seconds"))
                    .build();

            factory.closeConnection();

            return dish;
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        factory.closeConnection();
        return null;
    }

    @Override
    public Dish findById(Long id) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlDishStatements.dishFindById);

            sqlStatement.setLong(1, id);

            var result = sqlStatement.executeQuery();
            result.next();

            var dish = Dish.builder()
                    .id(result.getLong("id"))
                    .title(result.getString("title"))
                    .amount(result.getLong("amount"))
                    .price(result.getLong("price"))
                    .durationSeconds(result.getLong("duration_seconds"))
                    .build();

            factory.closeConnection();

            return dish;
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        factory.closeConnection();
        return null;
    }

    @Override
    public boolean add(Dish dish) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlDishStatements.addDish);

            sqlStatement.setString(1, dish.getTitle());
            sqlStatement.setLong(2, dish.getPrice());
            sqlStatement.setLong(3, dish.getAmount());
            sqlStatement.setLong(4, dish.getDurationSeconds());

            var result = sqlStatement.executeUpdate();

            factory.closeConnection();

            // one row was affected
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        factory.closeConnection();
        return false;
    }

    @Override
    public boolean remove(Dish dish) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlDishStatements.removeDish);

            sqlStatement.setLong(1, dish.getId());

            var result = sqlStatement.executeUpdate();

            factory.closeConnection();

            // one row was affected
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        factory.closeConnection();
        return false;
    }

    @Override
    public Dish update(Dish dish) {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlDishStatements.updateDish);

            sqlStatement.setLong(1, dish.getPrice());
            sqlStatement.setLong(2, dish.getAmount());
            sqlStatement.setLong(3, dish.getDurationSeconds());
            sqlStatement.setLong(4, dish.getId());

            var result = sqlStatement.executeUpdate();
            factory.closeConnection();
            // one row was affected
            return result == 1 ? dish : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        factory.closeConnection();
        return null;
    }

    @Override
    public List<Dish> getAll() {
        var connection = factory.getConnection();

        try {
            var sqlStatement = connection.prepareStatement(SqlDishStatements.getAllDishes);
            var result = sqlStatement.executeQuery();

            List<Dish> dishes = new ArrayList<>();
            while(result.next()) {
                dishes.add(Dish.builder()
                        .id(result.getLong("id"))
                        .title(result.getString("title"))
                        .amount(result.getLong("amount"))
                        .price(result.getLong("price"))
                        .durationSeconds(result.getLong("duration_seconds"))
                        .build());
            }
            factory.closeConnection();

            return dishes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        factory.closeConnection();
        return null;
    }
}
