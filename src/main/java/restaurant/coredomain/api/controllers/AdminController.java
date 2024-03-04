package restaurant.coredomain.api.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import restaurant.auth.api.controllers.AuthController;
import restaurant.auth.http.models.request.LoginRequest;
import restaurant.auth.services.AuthService;
import restaurant.coredomain.domain.repositories.ITransactionRepository;
import restaurant.coredomain.http.models.admin.responses.TotalProfit;

import java.util.Objects;

import static restaurant.coredomain.domain.entities.enums.TransactionType.FINISHED;

@Controller
@Scope(value = "prototype")
@RequestMapping("/admin")
public class AdminController {

    private final AuthService authService;
    private final ITransactionRepository transactionRepository;


    public AdminController(AuthService authService, ITransactionRepository transactionRepository) {
        this.authService = authService;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/{email}/{password}")
    public ResponseEntity<TotalProfit> getProfit(@PathVariable String email, @PathVariable String password) {
        var authResponse = authService.login(new LoginRequest(
                email,
                password));

        if (authResponse == null || !authResponse.isAuthenticated())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        if (!authResponse.isAdmin())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        var completedOrders = transactionRepository.findAll();
        long profit = 0;
        for (var item : completedOrders) {
            if (Objects.equals(item.getStatus(), FINISHED)) {
                profit += item.getEarnings();
            }
        }

        return new ResponseEntity<>(new TotalProfit(profit), HttpStatus.OK);
    }

}
