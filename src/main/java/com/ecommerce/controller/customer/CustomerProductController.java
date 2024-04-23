package com.ecommerce.controller.customer;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.dto.CompleteProductDetailDto;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.service.customer.product.CustomerProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerProductController {

    private final CustomerProductService customerProductService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categoryDtos = customerProductService.getAllCategories();
        return ResponseEntity.ok(categoryDtos);
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<List<ProductDto>> searchProductByTitle(@PathVariable("title") String title) {
        List<ProductDto> productDtos = customerProductService.searchProductByTitle(title);
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtos = customerProductService.getAllProducts();
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/products/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategoryId(@PathVariable Long categoryId) {
        List<ProductDto> productDtos = customerProductService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<CompleteProductDetailDto> getProductDetailById(@PathVariable Long productId) {
        CompleteProductDetailDto completeProductDetailDto = customerProductService.getCompleteProductDetailById(productId);
        if (completeProductDetailDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(completeProductDetailDto);
    }

}
