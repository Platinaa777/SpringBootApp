package restaurant.coredomain.http.models.dish.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDishResponse {
    private String title;
    private Long price;
    private Long amount;
    private Long durationSeconds;
}
