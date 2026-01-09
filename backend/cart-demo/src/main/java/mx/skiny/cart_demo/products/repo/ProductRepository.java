package mx.skiny.cart_demo.products.repo;

import mx.skiny.cart_demo.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository <Product, Long> {
}
