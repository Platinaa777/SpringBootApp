package restaurant.coredomain.domain.repositories;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import restaurant.coredomain.domain.entities.Order;

import java.util.List;

@Component
@Scope(value = "prototype")
public interface IOrderRepository {
    Long add(Order order);
    boolean remove(Long id);
    Order findOrder(String trasactionId);
    boolean updateOrderFinishedTime(Long id, Long duration);
}
