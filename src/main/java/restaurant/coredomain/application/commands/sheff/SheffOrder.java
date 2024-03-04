package restaurant.coredomain.application.commands.sheff;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.coredomain.application.responses.ResponseType;
import restaurant.coredomain.domain.entities.Dish;
import restaurant.coredomain.domain.entities.Order;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SheffOrder {
    private List<Dish> dishes;
    private String transactionId;
}
