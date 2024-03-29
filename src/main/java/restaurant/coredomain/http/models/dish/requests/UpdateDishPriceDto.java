package restaurant.coredomain.http.models.dish.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDishPriceDto {
    private String title;
    private Long price;
    private String email;
    private String password;
}
