package com.inventory.service;

import com.inventory.dto.DashboardStats;
import com.inventory.model.Product;
import com.inventory.model.Transaction;
import com.inventory.model.User;
import com.inventory.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Product management operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Get all products
     */
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

    /**
     * Get product by ID
     */
    public Optional<Product> getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id);
    }

    /**
     * Get product by product code
     */
    public Optional<Product> getProductByCode(String productCode) {
        log.info("Fetching product with code: {}", productCode);
        return productRepository.findByProductCode(productCode);
    }

    /**
     * Search products by name
     */
    public List<Product> searchProducts(String name) {
        log.info("Searching products with name containing: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Get products by category
     */
    public List<Product> getProductsByCategory(Long categoryId) {
        log.info("Fetching products for category: {}", categoryId);
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * Get products by supplier
     */
    public List<Product> getProductsBySupplier(Long supplierId) {
        log.info("Fetching products for supplier: {}", supplierId);
        return productRepository.findBySupplierId(supplierId);
    }

    /**
     * Get low stock products
     */
    public List<Product> getLowStockProducts() {
        log.info("Fetching low stock products");
        return productRepository.findLowStockProducts();
    }

    /**
     * Create new product
     */
    public Product createProduct(Product product) {
        log.info("Creating new product: {}", product.getName());

        // Check if product code already exists
        if (productRepository.existsByProductCode(product.getProductCode())) {
            throw new IllegalArgumentException("Product code already exists: " + product.getProductCode());
        }

        return productRepository.save(product);
    }

    /**
     * Update existing product
     */
    public Product updateProduct(Long id, Product updatedProduct) {
        log.info("Updating product with id: {}", id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        // Check if product code is being changed and if it already exists
        if (!existingProduct.getProductCode().equals(updatedProduct.getProductCode()) &&
                productRepository.existsByProductCode(updatedProduct.getProductCode())) {
            throw new IllegalArgumentException("Product code already exists: " + updatedProduct.getProductCode());
        }

        existingProduct.setProductCode(updatedProduct.getProductCode());
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setSupplier(updatedProduct.getSupplier());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setMinStockLevel(updatedProduct.getMinStockLevel());
        existingProduct.setUnit(updatedProduct.getUnit());

        return productRepository.save(existingProduct);
    }

    /**
     * Update product stock quantity
     */
    public Product updateProductStock(Long id, Integer quantity, User user, String notes) {
        log.info("Updating stock for product id: {} to quantity: {}", id, quantity);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        Integer previousQuantity = product.getQuantity();
        product.setQuantity(quantity);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionCode("TXN-" + System.currentTimeMillis());
        transaction.setProduct(product);
        transaction.setUser(user);
        transaction.setTransactionType(Transaction.TransactionType.ADJUSTMENT);
        transaction.setQuantity(Math.abs(quantity - previousQuantity));
        transaction.setUnitPrice(product.getPrice());
        transaction
                .setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(Math.abs(quantity - previousQuantity))));
        transaction.setPreviousQuantity(previousQuantity);
        transaction.setNewQuantity(quantity);
        transaction.setNotes(notes);

        transactionRepository.save(transaction);

        return productRepository.save(product);
    }

    /**
     * Delete product — blocked when the product has transaction history.
     */
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);

        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }

        if (transactionRepository.existsByProductId(id)) {
            throw new IllegalArgumentException(
                    "Cannot delete product: it has existing transaction records. Deactivate it instead.");
        }

        productRepository.deleteById(id);
    }

    /**
     * Get total inventory value
     */
    public BigDecimal getTotalInventoryValue() {
        return productRepository.getTotalInventoryValue();
    }
}
