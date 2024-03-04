package restaurant.coredomain.application.responses.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DishStatusResponse {
    private String title;
    private Long price;
    private Long amount;
}
