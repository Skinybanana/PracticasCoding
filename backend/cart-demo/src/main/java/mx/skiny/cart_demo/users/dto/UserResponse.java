package mx.skiny.cart_demo.users.dto;

public record UserResponse(
        Long id,
        String email,
        String username,
        String role
) {}
