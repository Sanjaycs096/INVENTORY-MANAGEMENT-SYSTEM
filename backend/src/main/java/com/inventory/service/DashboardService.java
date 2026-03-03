package com.inventory.service;

import com.inventory.dto.DashboardStats;
import com.inventory.model.Product;
import com.inventory.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for Dashboard statistics operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;

    /**
     * Get dashboard statistics
     */
    public DashboardStats getDashboardStats() {
        log.info("Fetching dashboard statistics");

        DashboardStats stats = new DashboardStats();

        // Get counts
        stats.setTotalProducts(productRepository.count());
        stats.setLowStockProducts(productRepository.countLowStockProducts());
        stats.setTotalCategories(categoryRepository.count());
        stats.setTotalSuppliers(supplierRepository.count());
        stats.setTotalTransactions(transactionRepository.count());

        // Get total inventory value
        stats.setTotalInventoryValue(productRepository.getTotalInventoryValue());

        // Get today's transactions count
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        stats.setTodayTransactions(transactionRepository.countTransactionsSince(startOfDay));

        // Get pending orders count
        stats.setPendingOrders(orderRepository.countPendingOrders());

        return stats;
    }

    /**
     * Returns products whose quantity is at or below their minimum stock level.
     */
    public List<Product> getLowStockProducts() {
        log.info("Fetching low stock products for dashboard");
        return productRepository.findLowStockProducts();
    }

    /**
     * Returns per-category product count and total inventory value.
     * Result format: [{category, productCount, totalValue}, ...]
     */
    public List<Map<String, Object>> getCategoryDistribution() {
        log.info("Fetching category distribution");
        List<Object[]> raw = categoryRepository.getCategoryDistribution();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : raw) {
            Map<String, Object> item = new HashMap<>();
            item.put("category", row[0]);
            item.put("productCount", ((Number) row[1]).longValue());
            item.put("totalValue",
                    row[2] instanceof BigDecimal ? row[2] : BigDecimal.valueOf(((Number) row[2]).doubleValue()));
            result.add(item);
        }
        return result;
    }
}
