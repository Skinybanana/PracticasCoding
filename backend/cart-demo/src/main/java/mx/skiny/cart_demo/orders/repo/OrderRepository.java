package mx.skiny.cart_demo.orders.repo;

import mx.skiny.cart_demo.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository <Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}
