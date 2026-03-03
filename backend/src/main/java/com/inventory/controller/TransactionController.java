package com.inventory.controller;

import com.inventory.dto.ApiResponse;
import com.inventory.dto.StockTransactionRequest;
import com.inventory.dto.TransactionDTO;
import com.inventory.model.Transaction;
import com.inventory.model.User;
import com.inventory.service.AuthService;
import com.inventory.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransactionController {

    private final TransactionService transactionService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getAllTransactions() {
        try {
            List<TransactionDTO> transactions = transactionService.getAllTransactions();
            return ResponseEntity.ok(ApiResponse.success(transactions));
        } catch (Exception e) {
            log.error("Failed to fetch transactions", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch transactions"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Transaction>> getTransactionById(@PathVariable Long id) {
        try {
            return transactionService.getTransactionById(id)
                    .map(transaction -> ResponseEntity.ok(ApiResponse.success(transaction)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Transaction not found")));
        } catch (Exception e) {
            log.error("Failed to fetch transaction with id: {}", id, e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch transaction"));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByProduct(@PathVariable Long productId) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByProduct(productId);
            return ResponseEntity.ok(ApiResponse.success(transactions));
        } catch (Exception e) {
            log.error("Failed to fetch transactions by product", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch transactions"));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByUser(@PathVariable Long userId) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByUser(userId);
            return ResponseEntity.ok(ApiResponse.success(transactions));
        } catch (Exception e) {
            log.error("Failed to fetch transactions by user", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch transactions"));
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByType(
            @PathVariable Transaction.TransactionType type) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByType(type);
            return ResponseEntity.ok(ApiResponse.success(transactions));
        } catch (Exception e) {
            log.error("Failed to fetch transactions by type", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch transactions"));
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success(transactions));
        } catch (Exception e) {
            log.error("Failed to fetch transactions by date range", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch transactions"));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getRecentTransactions() {
        try {
            List<TransactionDTO> transactions = transactionService.getRecentTransactions();
            return ResponseEntity.ok(ApiResponse.success(transactions));
        } catch (Exception e) {
            log.error("Failed to fetch recent transactions", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch recent transactions"));
        }
    }

    /**
     * POST /api/transactions/stock-in
     * Receives stock from supplier / warehouse. Increases product quantity.
     */
    @PostMapping("/stock-in")
    public ResponseEntity<ApiResponse<TransactionDTO>> stockIn(
            @Valid @RequestBody StockTransactionRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            TransactionDTO result = transactionService.processStockIn(
                    request.getProductId(),
                    request.getQuantity(),
                    currentUser,
                    request.getNotes());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Stock-in recorded successfully", result));
        } catch (IllegalArgumentException e) {
            log.error("Stock-in failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Stock-in error", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to process stock-in"));
        }
    }

    /**
     * POST /api/transactions/order
     * Issues / orders product (stock out). Prevents negative inventory.
     */
    @PostMapping("/order")
    public ResponseEntity<ApiResponse<TransactionDTO>> order(
            @Valid @RequestBody StockTransactionRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            TransactionDTO result = transactionService.processOrder(
                    request.getProductId(),
                    request.getQuantity(),
                    currentUser,
                    request.getNotes());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Order processed successfully", result));
        } catch (IllegalArgumentException e) {
            log.error("Order failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Order error", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to process order"));
        }
    }
}