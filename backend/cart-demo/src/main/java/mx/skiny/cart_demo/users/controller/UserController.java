package mx.skiny.cart_demo.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.skiny.cart_demo.users.dto.CreateUserRequest;
import mx.skiny.cart_demo.users.dto.UserResponse;
import mx.skiny.cart_demo.users.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse create (@Valid @RequestBody CreateUserRequest req){
        return userService.create(req);
    }

    @GetMapping("/{id}")
    public  UserResponse get(@PathVariable Long id){
        return userService.getById(id);
    }
}
