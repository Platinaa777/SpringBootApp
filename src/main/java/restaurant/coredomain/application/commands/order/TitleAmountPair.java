package restaurant.coredomain.application.commands.order;

import lombok.Data;

@Data
public class TitleAmountPair {
    private String title;
    private Long amount;
}
