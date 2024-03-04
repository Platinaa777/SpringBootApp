package restaurant.coredomain.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    private Long id;
    // name of dish
    private String title;
    private Long price;
    private Long amount;
    private Long durationSeconds;
    private List<Order> orders;
}
