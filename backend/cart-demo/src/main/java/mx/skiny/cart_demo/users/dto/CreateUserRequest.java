package mx.skiny.cart_demo.users.dto;

import jakarta.validation.constraints.*;

public record CreateUserRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 6, max = 100) String password
) {}