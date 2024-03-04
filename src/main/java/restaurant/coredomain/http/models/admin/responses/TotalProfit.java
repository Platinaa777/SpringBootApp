package restaurant.coredomain.http.models.admin.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TotalProfit {
    private Long profit;
}
