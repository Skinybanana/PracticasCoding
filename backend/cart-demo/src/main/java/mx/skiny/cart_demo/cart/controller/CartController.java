package mx.skiny.cart_demo.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.skiny.cart_demo.cart.dto.*;
import mx.skiny.cart_demo.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private Long userIdFromHeader(String userIdHeader) {
        try {
            return Long.parseLong(userIdHeader);
        } catch (Exception e) {
            throw new IllegalArgumentException("Missing/invalid X-USER-ID header");
        }
    }

    // GET /api/cart
    @GetMapping
    public CartResponse getCart(@RequestHeader("X-USER-ID") String userIdHeader) {
        return cartService.getCart(userIdFromHeader(userIdHeader));
    }

    // POST /api/cart/items
    @PostMapping("/items")
    public CartResponse addToCart(
            @RequestHeader("X-USER-ID") String userIdHeader,
            @Valid @RequestBody AddToCartRequest req
    ) {
        return cartService.addToCart(userIdFromHeader(userIdHeader), req);
    }

    // PATCH /api/cart/items/{productId}
    @PatchMapping("/items/{productId}")
    public CartResponse updateItem(
            @RequestHeader("X-USER-ID") String userIdHeader,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest req
    ) {
        return cartService.updateItem(userIdFromHeader(userIdHeader), productId, req);
    }

    // DELETE /api/cart/items/{productId}
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItem(
            @RequestHeader("X-USER-ID") String userIdHeader,
            @PathVariable Long productId
    ) {
        cartService.removeItem(userIdFromHeader(userIdHeader), productId);
        return ResponseEntity.noContent().build();
    }
}
