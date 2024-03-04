package restaurant.coredomain.domain.repositories;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import restaurant.coredomain.domain.entities.Transaction;

import java.util.List;

@Component
@Scope(value = "prototype")
public interface ITransactionRepository {
    String create(String email);
    boolean update(String id, Long totalEarning);
    Transaction find(String id);
    List<Transaction> findAll();
}
