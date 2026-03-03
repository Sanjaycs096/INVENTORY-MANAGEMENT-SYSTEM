package com.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for dashboard statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private Long totalProducts;
    private Long lowStockProducts;
    private Long totalCategories;
    private Long totalSuppliers;
    private Long totalTransactions;
    private BigDecimal totalInventoryValue;
    private Long todayTransactions;
    private Long pendingOrders;
}
