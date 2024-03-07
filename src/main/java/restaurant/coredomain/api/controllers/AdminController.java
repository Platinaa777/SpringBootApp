package restaurant.coredomain.api.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restaurant.auth.api.controllers.AuthController;
import restaurant.auth.http.models.request.LoginRequest;
import restaurant.auth.services.AuthService;
import restaurant.coredomain.domain.repositories.ITransactionRepository;
import restaurant.coredomain.http.models.admin.responses.ClientResponse;
import restaurant.coredomain.http.models.admin.responses.TotalProfit;

import javax.xml.parsers.SAXParser;
import java.util.List;
import java.util.Objects;

import static restaurant.coredomain.domain.entities.enums.TransactionType.FINISHED;

@RestController
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

    @GetMapping("profit/{email}/{password}")
    public ResponseEntity<TotalProfit> getProfit(@PathVariable String email, @PathVariable String password) {
        if (!checkIsAdmin(email, password)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        var completedOrders = transactionRepository.findAll();
        long profit = 0;
        for (var item : completedOrders) {
            if (Objects.equals(item.getStatus(), FINISHED)) {
                profit += item.getEarnings();
            }
        }

        return new ResponseEntity<>(new TotalProfit(profit), HttpStatus.OK);
    }

    @GetMapping("/users/{email}/{password}")
    public ResponseEntity<List<ClientResponse>> getAll(@PathVariable String email, @PathVariable String password) {
        if (!checkIsAdmin(email, password)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        var response = authService.getAll();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private boolean checkIsAdmin(String email, String password) {
        var authResponse = authService.login(new LoginRequest(
                email,
                password));

        if (authResponse == null || !authResponse.isAuthenticated())
            return false;

        return authResponse.isAdmin();
    }

}
