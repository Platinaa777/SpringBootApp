package restaurant.coredomain.http.models.order.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateOrderRequest {
    private String title;
    private Long amount;
    private String transactionId;
    private String email;
    private String password;
}
