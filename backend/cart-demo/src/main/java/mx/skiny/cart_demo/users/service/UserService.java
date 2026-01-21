package mx.skiny.cart_demo.users.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mx.skiny.cart_demo.cart.model.Cart;
import mx.skiny.cart_demo.cart.repo.CartRepository;
import mx.skiny.cart_demo.common.exception.NotFoundException;
import mx.skiny.cart_demo.users.dto.CreateUserRequest;
import mx.skiny.cart_demo.users.dto.UserResponse;
import mx.skiny.cart_demo.users.model.User;
import mx.skiny.cart_demo.users.model.UserRole;
import mx.skiny.cart_demo.users.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Transactional
    public UserResponse create(CreateUserRequest req){
        String passwordHash = "{noop}" + req.password();
        User user = User.builder()
                .email(req.email())
                .username(req.username())
                .passwordHash(passwordHash)
                .role(UserRole.USER)
                .build();

        user = userRepository.save(user);

        Cart cart = Cart.builder()
                .user(user)
                .build();

        cartRepository.save(cart);

        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return toResponse(u);
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getEmail(), u.getUsername(), u.getRole().name());
    }

}
