package restaurant.coredomain.http.models.order.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.coredomain.application.commands.order.TitleAmountPair;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    List<TitleAmountPair> dishes;
    private String clientEmail;
    private String password;
}
