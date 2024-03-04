package restaurant.coredomain.http.models.dish.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDishDto {
    private String title;
    private Long price;
    private Long amount;
    private Long durationSeconds;
    private String email;
    private String password;
}
