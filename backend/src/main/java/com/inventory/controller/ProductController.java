package com.inventory.controller;

import com.inventory.dto.ApiResponse;
import com.inventory.model.Product;
import com.inventory.model.User;
import com.inventory.service.AuthService;
import com.inventory.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for product management operations
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    private final ProductService productService;
    private final AuthService authService;

    /**
     * Get all products
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        try {
            log.info("Fetching all products");
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Failed to fetch products", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch products"));
        }
    }

    /**
     * Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        try {
            log.info("Fetching product with id: {}", id);
            return productService.getProductById(id)
                    .map(product -> ResponseEntity.ok(ApiResponse.success(product)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Product not found")));
        } catch (Exception e) {
            log.error("Failed to fetch product with id: {}", id, e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch product"));
        }
    }

    /**
     * Search products by name
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(@RequestParam String name) {
        try {
            log.info("Searching products with name: {}", name);
            List<Product> products = productService.searchProducts(name);
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Failed to search products", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to search products"));
        }
    }

    /**
     * Get products by category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(@PathVariable Long categoryId) {
        try {
            log.info("Fetching products for category: {}", categoryId);
            List<Product> products = productService.getProductsByCategory(categoryId);
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Failed to fetch products by category", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch products"));
        }
    }

    /**
     * Get products by supplier
     */
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsBySupplier(@PathVariable Long supplierId) {
        try {
            log.info("Fetching products for supplier: {}", supplierId);
            List<Product> products = productService.getProductsBySupplier(supplierId);
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Failed to fetch products by supplier", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch products"));
        }
    }

    /**
     * Get low stock products
     */
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<Product>>> getLowStockProducts() {
        try {
            log.info("Fetching low stock products");
            List<Product> products = productService.getLowStockProducts();
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Failed to fetch low stock products", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch low stock products"));
        }
    }

    /**
     * Create new product
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody Product product) {
        try {
            log.info("Creating new product: {}", product.getName());
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Product created successfully", createdProduct));
        } catch (IllegalArgumentException e) {
            log.error("Failed to create product: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Update existing product
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id,
            @Valid @RequestBody Product product) {
        try {
            log.info("Updating product with id: {}", id);
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updatedProduct));
        } catch (IllegalArgumentException e) {
            log.error("Failed to update product: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Update product stock
     */
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<Product>> updateProductStock(@PathVariable Long id,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String notes) {
        try {
            log.info("Updating stock for product id: {}", id);
            User currentUser = authService.getCurrentUser();
            Product updatedProduct = productService.updateProductStock(id, quantity, currentUser, notes);
            return ResponseEntity.ok(ApiResponse.success("Product stock updated successfully", updatedProduct));
        } catch (IllegalArgumentException e) {
            log.error("Failed to update product stock: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Delete product
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        try {
            log.info("Deleting product with id: {}", id);
            productService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
        } catch (IllegalArgumentException e) {
            log.error("Failed to delete product: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get total inventory value
     */
    @GetMapping("/inventory-value")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalInventoryValue() {
        try {
            log.info("Fetching total inventory value");
            BigDecimal totalValue = productService.getTotalInventoryValue();
            return ResponseEntity.ok(ApiResponse.success(totalValue));
        } catch (Exception e) {
            log.error("Failed to fetch inventory value", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch inventory value"));
        }
    }
}
