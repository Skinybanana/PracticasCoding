package mx.skiny.cart_demo.products.service;

import lombok.RequiredArgsConstructor;

import mx.skiny.cart_demo.products.dto.CreateProductRequest;
import mx.skiny.cart_demo.products.dto.ProductResponse;
import mx.skiny.cart_demo.products.model.Product;
import mx.skiny.cart_demo.products.repo.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    // Pageable = paginación + ordenamiento.
    // Divide resultados grandes en páginas (ej. 20 por página).
    // Spring lo traduce a LIMIT / OFFSET en SQL.
    @Transactional(readOnly = true)
    public Page<ProductResponse> list(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        return toResponse(p);
    }

    @Transactional
    public ProductResponse create(CreateProductRequest req) {
        Product p = Product.builder()
                .name(req.name())
                .price(req.price())
                .stock(req.stock())
                .active(true)
                .build();

        return toResponse(productRepository.save(p));
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getStock(),
                p.getActive()
        );
    }

}
