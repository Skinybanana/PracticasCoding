package mx.skiny.cart_demo.products.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.skiny.cart_demo.products.dto.CreateProductRequest;
import mx.skiny.cart_demo.products.dto.ProductResponse;
import mx.skiny.cart_demo.products.dto.UpdateProductRequest;
import mx.skiny.cart_demo.products.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    @GetMapping
    public Page<ProductResponse> list(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ){
        return productService.list(pageable);
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id){
        return productService.getById(id);
    }

    @PostMapping
    public ProductResponse create(@Valid @RequestBody CreateProductRequest req){
        return productService.create(req);
    }

    @PutMapping("/{id}")
    public ProductResponse update (@PathVariable Long id, @Valid @RequestBody UpdateProductRequest req){
        return productService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
