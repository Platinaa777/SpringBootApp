package restaurant.coredomain.api.mappers;

import restaurant.coredomain.domain.entities.Dish;
import restaurant.coredomain.http.models.dish.responses.GetDishResponse;
import restaurant.coredomain.http.models.dish.responses.PutDishResponse;

import java.util.ArrayList;
import java.util.List;

public class PresentationMapper {

    public static List<GetDishResponse> mapToList(List<Dish> dishes) {
        List<GetDishResponse> result = new ArrayList<>();

        for (var item : dishes) {
            result.add(GetDishResponse.builder()
                    .price(item.getPrice())
                    .title(item.getTitle())
                    .amount(item.getAmount())
                    .durationSeconds(item.getDurationSeconds())
                    .build());
        }

        return result;
    }

    public static GetDishResponse toView(Dish dish) {

        return GetDishResponse.builder()
                    .price(dish.getPrice())
                    .title(dish.getTitle())
                    .amount(dish.getAmount())
                    .durationSeconds(dish.getDurationSeconds())
                    .build();
    }

    public static PutDishResponse toPutView(Dish dish) {

        return PutDishResponse.builder()
                .price(dish.getPrice())
                .title(dish.getTitle())
                .amount(dish.getAmount())
                .durationSeconds(dish.getDurationSeconds())
                .build();
    }
}
