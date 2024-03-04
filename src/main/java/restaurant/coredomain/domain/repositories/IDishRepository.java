package restaurant.coredomain.domain.repositories;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import restaurant.coredomain.domain.entities.Dish;

import java.util.List;

@Component
@Scope(value = "prototype")
public interface IDishRepository {
    Dish findByTitle(String title);
    Dish findById(Long id);
    boolean add(Dish dish);
    boolean remove(Dish dish);
    Dish update(Dish dish);
    List<Dish> getAll();
}
