package restaurant.coredomain.http.models.order.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RemoveOrderRequest {
    private String email;
    private String password;
    private String transactionId;
}
