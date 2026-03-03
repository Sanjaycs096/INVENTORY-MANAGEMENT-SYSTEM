package com.inventory.repository;

import com.inventory.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Supplier entity
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    Optional<Supplier> findByEmail(String email);

    List<Supplier> findByNameContainingIgnoreCase(String name);

    boolean existsByEmail(String email);
}
