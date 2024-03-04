package restaurant.auth.mappers;

import restaurant.auth.enums.RoleType;
import restaurant.auth.http.models.request.AdminRegisterRequest;
import restaurant.auth.http.models.request.RegisterRequest;
import restaurant.auth.services.models.RegisterUserCommand;

public class HttpRequestMapper {

    public static RegisterUserCommand mapFromAdmin(AdminRegisterRequest req) {
        return new RegisterUserCommand(req.getEmail(), req.getPassword(), RoleType.Admin);
    }

    public static RegisterUserCommand mapFromUser(RegisterRequest req) {
        return new RegisterUserCommand(req.getEmail(), req.getPassword(), RoleType.Client);
    }
}
