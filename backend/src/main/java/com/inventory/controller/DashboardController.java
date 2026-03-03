package com.inventory.controller;

import com.inventory.dto.ApiResponse;
import com.inventory.dto.DashboardStats;
import com.inventory.model.Product;
import com.inventory.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for dashboard operations
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * GET /api/dashboard — primary entry point (also aliased as /stats)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<DashboardStats>> getDashboard() {
        return getDashboardStats();
    }

    /**
     * GET /api/dashboard/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStats>> getDashboardStats() {
        try {
            log.info("Fetching dashboard statistics");
            DashboardStats stats = dashboardService.getDashboardStats();
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            log.error("Failed to fetch dashboard statistics", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch dashboard statistics"));
        }
    }

    /**
     * GET /api/dashboard/low-stock
     * Returns all products whose quantity <= minStockLevel.
     */
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<Product>>> getLowStock() {
        try {
            log.info("Fetching low stock products");
            List<Product> products = dashboardService.getLowStockProducts();
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Failed to fetch low stock products", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch low stock products"));
        }
    }

    /**
     * GET /api/dashboard/category-distribution
     * Returns per-category product count and total inventory value.
     */
    @GetMapping("/category-distribution")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCategoryDistribution() {
        try {
            log.info("Fetching category distribution");
            List<Map<String, Object>> distribution = dashboardService.getCategoryDistribution();
            return ResponseEntity.ok(ApiResponse.success(distribution));
        } catch (Exception e) {
            log.error("Failed to fetch category distribution", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch category distribution"));
        }
    }
}
