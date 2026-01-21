package mx.skiny.cart_demo.products.service;

import lombok.RequiredArgsConstructor;

import mx.skiny.cart_demo.common.exception.NotFoundException;
import mx.skiny.cart_demo.products.dto.CreateProductRequest;
import mx.skiny.cart_demo.products.dto.ProductResponse;
import mx.skiny.cart_demo.products.dto.UpdateProductRequest;
import mx.skiny.cart_demo.products.model.Product;
import mx.skiny.cart_demo.products.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class ProductService {

    final private ProductRepository productRepository;

    // Pageable = paginación + ordenamiento.
    // Divide resultados grandes en páginas (ej. 20 por página).
    // Spring lo traduce a LIMIT / OFFSET en SQL.
    @Transactional(readOnly = true)
    public Page<ProductResponse> list(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Product p = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
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

    @Transactional
    public ProductResponse update(Long id, UpdateProductRequest req){
        Product p = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found: " + id));
        p.setName(req.name());
        p.setPrice(req.price());
        p.setStock(req.stock());
        p.setActive(req.active());
        return toResponse(productRepository.save(p));
    }

    @Transactional
    public void delete (Long id){
        Product p = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found: " + id));
        p.setActive(false);
        productRepository.save(p);
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
