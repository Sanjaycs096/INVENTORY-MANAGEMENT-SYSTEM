package com.inventory.repository;

import com.inventory.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Transaction entity
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionCode(String transactionCode);

    // Sorted newest-first — used by getAllTransactions()
    List<Transaction> findAllByOrderByTransactionDateDesc();

    List<Transaction> findByProductId(Long productId);

    boolean existsByProductId(Long productId);

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByTransactionType(Transaction.TransactionType transactionType);

    @Query("SELECT t FROM Transaction t WHERE t.transactionDate >= :startDate AND t.transactionDate <= :endDate ORDER BY t.transactionDate DESC")
    List<Transaction> findByDateRange(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.transactionDate >= :startDate")
    Long countTransactionsSince(@Param("startDate") LocalDateTime startDate);

    List<Transaction> findTop10ByOrderByTransactionDateDesc();
}
