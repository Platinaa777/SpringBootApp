package restaurant.coredomain.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import restaurant.auth.http.models.request.LoginRequest;
import restaurant.auth.services.AuthService;
import restaurant.coredomain.api.validators.RequestValidator;
import restaurant.coredomain.api.mappers.PresentationMapper;
import restaurant.coredomain.application.services.DishService;
import restaurant.coredomain.http.models.dish.requests.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import restaurant.coredomain.http.models.dish.responses.GetDishResponse;
import restaurant.coredomain.http.models.dish.responses.PutDishResponse;

import java.util.List;


@Controller
@Scope(value = "prototype")
@RequestMapping("/dish")
public class DishController {

    private final DishService dishService;
    private final AuthService authService;

    @Autowired
    public DishController(DishService dishService, AuthService authService) {

        this.dishService = dishService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody CreateDishDto dto) {
        var responseAuth = authService.login(LoginRequest.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build());

        if (responseAuth == null || !responseAuth.isAuthenticated() || !responseAuth.isAdmin())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);


        var result = dishService.create(dto);
        if (!result)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>("dish is created", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> removeDishFromMenu(@RequestBody DeleteDishRequest req) {
        var responseAuth = authService.login(LoginRequest.builder()
                .email(req.getEmail())
                .password(req.getPassword())
                .build());

        if (!responseAuth.isAuthenticated() || !responseAuth.isAdmin())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        var result = dishService.delete(req.getId());

        if (result)
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);

        return new ResponseEntity<>("Dish not found", HttpStatus.NOT_FOUND);
    }

    // all people can see our menu (because everyone firstly want to see what we suggest ant then register if our menu is appropriate for them)
    @GetMapping
    public ResponseEntity<List<GetDishResponse>> getAll() {
        var result = dishService.getAll();
        return new ResponseEntity<>(PresentationMapper.mapToList(result), HttpStatus.OK);
    }

    // test endpoint
    @GetMapping("/title/{title}")
    public ResponseEntity<GetDishResponse> getDish(@PathVariable String title) {
        var dish = dishService.findDishByTitle(title);
        if (dish == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(PresentationMapper.toView(dish), HttpStatus.OK);
    }

    @PutMapping("/price")
    public ResponseEntity<PutDishResponse> editPrice(@RequestBody UpdateDishPriceDto req) {
        var responseAuth = authService.login(LoginRequest.builder()
                .email(req.getEmail())
                .password(req.getPassword())
                .build());

        if (!responseAuth.isAuthenticated() || !responseAuth.isAdmin())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        if (req.getPrice() <= 0)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        var dish = dishService.updatePrice(req);

        if (dish == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(PresentationMapper.toPutView(dish), HttpStatus.OK);
    }

    @PutMapping("/amount")
    public ResponseEntity<PutDishResponse> editAmount(@RequestBody UpdateDishAmountDto dto) {
        var responseAuth = authService.login(LoginRequest.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build());

        if (!responseAuth.isAuthenticated() || !responseAuth.isAdmin())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        if (dto.getAmount() <= 0)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        var dish = dishService.updateAmount(dto);

        if (dish == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(PresentationMapper.toPutView(dish), HttpStatus.OK);

    }

    @PutMapping("/duration")
    public ResponseEntity<PutDishResponse> editDuration(@RequestBody UpdateDishDurationDto dto) {
        var responseAuth = authService.login(LoginRequest.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build());

        if (!responseAuth.isAuthenticated() || !responseAuth.isAdmin())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        if (dto.getDuration() <= 0)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        var dish = dishService.updateDuration(dto);

        if (dish == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(PresentationMapper.toPutView(dish), HttpStatus.OK);
    }
}

