package restaurant.coredomain.application.commands.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrder {
    private List<TitleAmountPair> dishes;
    private String transactionId;
    private String clientEmail;
}