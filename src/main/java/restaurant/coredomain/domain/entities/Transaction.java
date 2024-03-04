package restaurant.coredomain.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String id;
    // money which was paid when order was successfully closed
    private Long earnings;
    // IN_PROGRESS, FINISHED
    private String status;
    private String clientEmail;
}
