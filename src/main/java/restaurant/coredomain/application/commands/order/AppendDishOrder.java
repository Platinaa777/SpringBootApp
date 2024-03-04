package restaurant.coredomain.application.commands.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AppendDishOrder {
    private String title;
    private Long amount;
    private String email;
    private String transactionId;
}
