package restaurant.coredomain.api.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import restaurant.auth.http.models.request.LoginRequest;
import restaurant.auth.services.AuthService;
import restaurant.coredomain.application.commands.order.AppendDishOrder;
import restaurant.coredomain.application.commands.order.CreateOrder;
import restaurant.coredomain.application.responses.status.OrderStatusResponse;
import restaurant.coredomain.application.services.OrderHandler;
import restaurant.coredomain.http.models.order.requests.CreateOrderRequest;
import restaurant.coredomain.http.models.order.requests.RemoveOrderRequest;
import restaurant.coredomain.http.models.order.requests.UpdateOrderRequest;
@RestController
@Controller
@RequestMapping("/order")
@Scope(value = "prototype")
public class OrderController {

    private final OrderHandler orderHandler;
    private final AuthService authService;

    public OrderController(OrderHandler orderHandler, AuthService authService) {
        this.orderHandler = orderHandler;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        var authResponse = authService.login(new LoginRequest(
                createOrderRequest.getClientEmail(),
                createOrderRequest.getPassword()));

        if (authResponse == null || !authResponse.isAuthenticated())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);


        var response = orderHandler.createOrder(CreateOrder.builder()
                .clientEmail(createOrderRequest.getClientEmail())
                .dishes(createOrderRequest.getDishes())
                .build());

        if (!response.isSuccess) {
            return new ResponseEntity<>(String.join("\n", response.messages), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.join("\n", response.messages), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> removeOrder(@RequestBody RemoveOrderRequest req) {
        var authResponse = authService.login(new LoginRequest(
                req.getEmail(),
                req.getPassword()));

        if (authResponse == null || !authResponse.isAuthenticated())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        var response = orderHandler.cancelOrder(req.getTransactionId());

        if (response) {
            return new ResponseEntity<>("OK! Order was removed", HttpStatus.OK);
        }
        return new ResponseEntity<>("Order cant be removed because time was expired", HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<String> addDishToOrder(@RequestBody UpdateOrderRequest req) {
        var authResponse = authService.login(new LoginRequest(
                req.getEmail(),
                req.getPassword()));

        if (authResponse == null || !authResponse.isAuthenticated())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);


        var response = orderHandler.appendDishToOrder(AppendDishOrder.builder()
                .title(req.getTitle())
                .amount(req.getAmount())
                .email(req.getEmail())
                .transactionId(req.getTransactionId())
                .build());

        if (!response.isSuccess) {
            return new ResponseEntity<>(String.join("\n", response.messages), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.join("\n", response.messages), HttpStatus.OK);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<OrderStatusResponse> getInfoAboutOrder(@PathVariable String transactionId) {
        var response = orderHandler.findOrder(transactionId);

        if (response == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/pay/{transactionId}")
    public ResponseEntity payOrder(@PathVariable String transactionId) {
        var response = orderHandler.pay(transactionId);

        if (!response.isSuccess) {
            return new ResponseEntity<>(String.join("\n", response.messages), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.join("\n", response.messages), HttpStatus.OK);
    }
}
