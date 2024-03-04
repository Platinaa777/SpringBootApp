package restaurant.coredomain.application.responses.status;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import restaurant.coredomain.domain.entities.Dish;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderStatusResponse {
    private boolean isReady;
    private Timestamp started_at;
    private Timestamp finished_at;
    private List<DishStatusResponse> dishes;
}
