package restaurant.coredomain.http.models.dish.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDishDurationDto {
    private String title;
    private Long duration;
    private String email;
    private String password;
}
