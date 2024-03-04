package restaurant.coredomain.http.models.dish.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDishAmountDto {
    private Long id;
    private Long amount;
    private String email;
    private String password;
}
