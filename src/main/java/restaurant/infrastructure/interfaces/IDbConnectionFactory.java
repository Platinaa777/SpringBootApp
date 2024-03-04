package restaurant.infrastructure.interfaces;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public interface IDbConnectionFactory<TConnection> {
    TConnection getConnection();
    void closeConnection();
}
