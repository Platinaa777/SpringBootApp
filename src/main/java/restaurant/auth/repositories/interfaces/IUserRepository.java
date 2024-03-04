package restaurant.auth.repositories.interfaces;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import restaurant.auth.models.Client;

@Component
@Scope(value = "prototype")
public interface IUserRepository {
    boolean create(Client user);
    boolean isExist(Client user);
    Client find(String username);
}
