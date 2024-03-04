package restaurant.auth.api.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import restaurant.auth.http.models.request.AdminRegisterRequest;
import restaurant.auth.http.models.request.LoginRequest;
import restaurant.auth.http.models.request.RegisterRequest;
import restaurant.auth.http.models.responses.AuthResponse;
import restaurant.auth.mappers.HttpRequestMapper;
import restaurant.auth.services.AuthService;
import restaurant.auth.services.models.RegisterUserCommand;

import java.util.Objects;

@Controller()
@RequestMapping("/auth")
@Scope(value = "prototype")
public class AuthController {
    private final static String securityCode = "admin777";
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        var result = authService.create(HttpRequestMapper.mapFromUser(registerRequest));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest) {
        var result = authService.login(loginRequest);

        if (result == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody AdminRegisterRequest registerRequest) {
        if (!Objects.equals(registerRequest.getCode(), securityCode))
            return new ResponseEntity<>(new AuthResponse(), HttpStatus.UNAUTHORIZED);

        var result = authService.create(HttpRequestMapper.mapFromAdmin(registerRequest));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
