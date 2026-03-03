package com.inventory.repository;

import com.inventory.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    List<Category> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);

    /**
     * Returns category name, product count, and total inventory value per category.
     * Used for the dashboard Category Distribution table.
     */
    @Query(value = "SELECT c.name AS category, COUNT(p.id) AS productCount, " +
            "COALESCE(SUM(p.price * p.quantity), 0) AS totalValue " +
            "FROM categories c LEFT JOIN products p ON p.category_id = c.id " +
            "GROUP BY c.id, c.name ORDER BY productCount DESC", nativeQuery = true)
    List<Object[]> getCategoryDistribution();
}
