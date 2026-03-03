package com.inventory.repository;

import com.inventory.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = { "category", "supplier" })
    List<Product> findAll();

    @EntityGraph(attributePaths = { "category", "supplier" })
    Optional<Product> findByProductCode(String productCode);

    @EntityGraph(attributePaths = { "category", "supplier" })
    List<Product> findByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = { "category", "supplier" })
    List<Product> findByCategoryId(Long categoryId);

    @EntityGraph(attributePaths = { "category", "supplier" })
    List<Product> findBySupplierId(Long supplierId);

    @EntityGraph(attributePaths = { "category", "supplier" })
    @Query("SELECT p FROM Product p WHERE p.quantity < 10 OR p.quantity <= p.minStockLevel")
    List<Product> findLowStockProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity < 10 OR p.quantity <= p.minStockLevel")
    Long countLowStockProducts();

    @Query("SELECT COALESCE(SUM(p.quantity * p.price), 0) FROM Product p")
    java.math.BigDecimal getTotalInventoryValue();

    boolean existsByProductCode(String productCode);

    long countByCategoryId(Long categoryId);

    long countBySupplierId(Long supplierId);
}
