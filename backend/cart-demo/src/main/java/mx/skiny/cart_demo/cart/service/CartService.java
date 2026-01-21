package mx.skiny.cart_demo.cart.service;

import lombok.RequiredArgsConstructor;
import mx.skiny.cart_demo.cart.dto.AddToCartRequest;
import mx.skiny.cart_demo.cart.dto.CartItemResponse;
import mx.skiny.cart_demo.cart.dto.CartResponse;
import mx.skiny.cart_demo.cart.dto.UpdateCartItemRequest;
import mx.skiny.cart_demo.cart.model.Cart;
import mx.skiny.cart_demo.cart.model.CartItem;
import mx.skiny.cart_demo.cart.repo.CartItemRepository;
import mx.skiny.cart_demo.cart.repo.CartRepository;
import mx.skiny.cart_demo.common.exception.NotFoundException;
import mx.skiny.cart_demo.products.model.Product;
import mx.skiny.cart_demo.products.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found for user: "));

        List<CartItemResponse> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cart.getItems()){
            Product p = ci.getProduct();
            BigDecimal lineTotal = p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            total = total.add(lineTotal);
            items.add(new CartItemResponse(
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    ci.getQuantity(),
                    lineTotal
            ));
        }
        return new CartResponse(cart.getId(), userId, items, total);

    }

    @Transactional
    public CartResponse addToCart(Long userId, AddToCartRequest req){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found for user: " + userId));

        Product product = productRepository.findByIdAndActiveTrue(req.productId())
                .orElseThrow(() -> new NotFoundException("Product not found: " + req.productId()));

        int addQty = req.quantity();

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);
        int newQty = (item == null) ? addQty : item.getQuantity() + addQty;

        if (newQty > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock. requested=" + newQty + ", available=" + product.getStock());
        }

        if (item == null) {
            CartItem ci = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(newQty)
                    .build();
            cartItemRepository.save(ci);
        } else {
            item.setQuantity(newQty);
            cartItemRepository.save(item);
        }

        return getCart(userId);

    }

    @Transactional
    public CartResponse updateItem(Long userId, Long productId, UpdateCartItemRequest req) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found for user: " + userId));

        Product product = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new NotFoundException("Cart item not found for product: " + productId));

        int qty = req.quantity();

        if (qty == 0) {
            cartItemRepository.delete(item);
            return getCart(userId);
        }

        if (qty > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock. requested=" + qty + ", available=" + product.getStock());
        }

        item.setQuantity(qty);
        cartItemRepository.save(item);

        return getCart(userId);
    }

    @Transactional
    public void removeItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found for user: " + userId));

        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);
    }
}
