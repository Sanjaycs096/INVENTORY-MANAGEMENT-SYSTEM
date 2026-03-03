package com.inventory.service;

import com.inventory.dto.TransactionDTO;
import com.inventory.model.Product;
import com.inventory.model.Transaction;
import com.inventory.model.User;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;

    /**
     * Return all transactions sorted newest-first.
     */
    public List<TransactionDTO> getAllTransactions() {
        log.info("Fetching all transactions");
        return transactionRepository.findAllByOrderByTransactionDateDesc().stream()
                .map(TransactionDTO::from)
                .collect(Collectors.toList());
    }

    public Optional<Transaction> getTransactionById(Long id) {
        log.info("Fetching transaction with id: {}", id);
        return transactionRepository.findById(id);
    }

    public List<TransactionDTO> getTransactionsByProduct(Long productId) {
        log.info("Fetching transactions for product: {}", productId);
        return transactionRepository.findByProductId(productId).stream()
                .map(TransactionDTO::from)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionsByUser(Long userId) {
        log.info("Fetching transactions for user: {}", userId);
        return transactionRepository.findByUserId(userId).stream()
                .map(TransactionDTO::from)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionsByType(Transaction.TransactionType type) {
        log.info("Fetching transactions of type: {}", type);
        return transactionRepository.findByTransactionType(type).stream()
                .map(TransactionDTO::from)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching transactions from {} to {}", startDate, endDate);
        return transactionRepository.findByDateRange(startDate, endDate).stream()
                .map(TransactionDTO::from)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getRecentTransactions() {
        log.info("Fetching recent transactions");
        return transactionRepository.findTop10ByOrderByTransactionDateDesc().stream()
                .map(TransactionDTO::from)
                .collect(Collectors.toList());
    }

    public Transaction createTransaction(Transaction transaction) {
        log.info("Creating new transaction: {}", transaction.getTransactionCode());
        return transactionRepository.save(transaction);
    }

    // =========================================================
    // BUSINESS LOGIC: STOCK IN
    // Increases product quantity and records a STOCK_IN transaction.
    // =========================================================
    @Transactional
    public TransactionDTO processStockIn(Long productId, Integer quantity, User user, String notes) {
        log.info("Processing STOCK_IN for product {}: +{}", productId, quantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        int prevQty = product.getQuantity();
        int newQty = prevQty + quantity;

        // Persist updated stock
        product.setQuantity(newQty);
        productRepository.save(product);

        // Record the transaction
        Transaction txn = new Transaction();
        txn.setTransactionCode("TXN-IN-" + System.currentTimeMillis());
        txn.setProduct(product);
        txn.setUser(user);
        txn.setTransactionType(Transaction.TransactionType.STOCK_IN);
        txn.setQuantity(quantity);
        txn.setUnitPrice(product.getPrice());
        txn.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        txn.setPreviousQuantity(prevQty);
        txn.setNewQuantity(newQty);
        txn.setNotes(notes);

        Transaction saved = transactionRepository.save(txn);
        log.info("STOCK_IN complete: product {} qty {} → {}", productId, prevQty, newQty);
        return TransactionDTO.from(saved);
    }

    // =========================================================
    // BUSINESS LOGIC: ISSUE / ORDER (STOCK OUT)
    // Decreases product quantity. Prevents negative inventory.
    // Records a STOCK_OUT transaction.
    // =========================================================
    @Transactional
    public TransactionDTO processOrder(Long productId, Integer quantity, User user, String notes) {
        log.info("Processing STOCK_OUT (order) for product {}: -{}", productId, quantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        // Prevent negative inventory
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock. Available: " + product.getQuantity() + ", Requested: " + quantity);
        }

        int prevQty = product.getQuantity();
        int newQty = prevQty - quantity;

        // Persist updated stock (atomic update with quantity check)
        product.setQuantity(newQty);
        productRepository.save(product);

        // Record the transaction
        Transaction txn = new Transaction();
        txn.setTransactionCode("TXN-OUT-" + System.currentTimeMillis());
        txn.setProduct(product);
        txn.setUser(user);
        txn.setTransactionType(Transaction.TransactionType.STOCK_OUT);
        txn.setQuantity(quantity);
        txn.setUnitPrice(product.getPrice());
        txn.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        txn.setPreviousQuantity(prevQty);
        txn.setNewQuantity(newQty);
        txn.setNotes(notes);

        Transaction saved = transactionRepository.save(txn);
        log.info("STOCK_OUT complete: product {} qty {} → {}", productId, prevQty, newQty);
        return TransactionDTO.from(saved);
    }
}