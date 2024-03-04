package restaurant.auth.services;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import restaurant.auth.enums.RoleType;
import restaurant.auth.http.models.request.LoginRequest;
import restaurant.auth.http.models.responses.AuthResponse;
import restaurant.auth.models.Client;
import restaurant.auth.repositories.interfaces.IUserRepository;
import restaurant.auth.services.models.RegisterUserCommand;

@Service
@AllArgsConstructor
@Scope(value = "prototype")
public class AuthService {
    private final IUserRepository userRepository;

    public AuthResponse create(RegisterUserCommand command) {
        var existingUser = userRepository.isExist(Client.builder()
                .email(command.getEmail())
                .password(command.getPassword())
                .role(command.getRole())
                .build());

        if (existingUser)
            return new AuthResponse(command.getEmail(), false);

        var result = userRepository.create(Client.builder()
                .email(command.getEmail())
                .password(command.getPassword())
                .role(command.getRole())
                .build());

        return new AuthResponse(command.getEmail(), result, command.getRole().equals(RoleType.Admin));
    }

    public AuthResponse login(LoginRequest req) {
        var result = userRepository.find(req.getEmail());

        if (result == null)
            return null;


        return new AuthResponse(req.getEmail(), result.getPassword().equals(req.getPassword()), result.getRole().equals(RoleType.Admin));
    }

    public Client findByEmail(String email) {
        return userRepository.find(email);
    }
}
