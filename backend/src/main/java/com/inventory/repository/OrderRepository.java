package com.inventory.repository;

import com.inventory.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order entity
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderCode(String orderCode);

    List<Order> findByUserId(Long userId);

    List<Order> findBySupplierId(Long supplierId);

    List<Order> findByOrderStatus(Order.OrderStatus orderStatus);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = 'PENDING'")
    Long countPendingOrders();

    List<Order> findTop10ByOrderByCreatedAtDesc();
}
