package restaurant.coredomain.http.models.transaction.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloseTransactionResponse {
    private boolean isClosed;
}
