package restaurant.coredomain.application.services;


import org.springframework.context.annotation.Scope;
import restaurant.coredomain.domain.entities.Dish;
import restaurant.coredomain.domain.repositories.IDishRepository;
import restaurant.coredomain.http.models.dish.requests.CreateDishDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import restaurant.coredomain.http.models.dish.requests.UpdateDishAmountDto;
import restaurant.coredomain.http.models.dish.requests.UpdateDishDurationDto;
import restaurant.coredomain.http.models.dish.requests.UpdateDishPriceDto;

import java.util.List;

@Service
@AllArgsConstructor
@Scope(value = "prototype")
public class DishService {
    private final IDishRepository dishRepository;

    public Dish findDishByTitle(String title) {
        return dishRepository.findByTitle(title);
    }

    public List<Dish> getAll() {
        return dishRepository.getAll();
    }

    public boolean create(CreateDishDto dishDto) {
        var dish = Dish.builder()
                .title(dishDto.getTitle())
                .amount(dishDto.getAmount())
                .price(dishDto.getPrice())
                .durationSeconds(dishDto.getDurationSeconds())
                .build();

        var existingDish = dishRepository.findByTitle(dishDto.getTitle());

        if (existingDish != null)
            return false;

        return dishRepository.add(dish);
    }

    public Dish updateAmount(UpdateDishAmountDto dish) {
        var existingDish = dishRepository.findById(dish.getId());

        if (existingDish == null)
            return null;

        existingDish.setAmount(dish.getAmount());
        return dishRepository.update(existingDish);
    }

    public Dish updatePrice(UpdateDishPriceDto dish) {
        var existingDish = dishRepository.findById(dish.getId());

        if (existingDish == null)
            return null;

        existingDish.setAmount(dish.getPrice());
        return dishRepository.update(existingDish);
    }

    public Dish updateDuration(UpdateDishDurationDto dish) {
        var existingDish = dishRepository.findById(dish.getId());

        if (existingDish == null)
            return null;

        existingDish.setAmount(dish.getDuration());
        return dishRepository.update(existingDish);
    }


    public boolean delete(Long id) {
        var existingDish = dishRepository.findById(id);

        if (existingDish == null)
            return false;

        return dishRepository.remove(existingDish);
    }
}
