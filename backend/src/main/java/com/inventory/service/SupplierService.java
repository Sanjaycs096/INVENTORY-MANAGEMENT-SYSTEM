package com.inventory.service;

import com.inventory.dto.SupplierDTO;
import com.inventory.model.Supplier;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    public List<SupplierDTO> getAllSuppliers() {
        log.info("Fetching all suppliers");
        return supplierRepository.findAll().stream()
                .map(s -> SupplierDTO.from(s, productRepository.countBySupplierId(s.getId())))
                .collect(Collectors.toList());
    }

    public Optional<Supplier> getSupplierById(Long id) {
        log.info("Fetching supplier with id: {}", id);
        return supplierRepository.findById(id);
    }

    public List<SupplierDTO> searchSuppliers(String name) {
        log.info("Searching suppliers with name containing: {}", name);
        return supplierRepository.findByNameContainingIgnoreCase(name).stream()
                .map(s -> SupplierDTO.from(s, productRepository.countBySupplierId(s.getId())))
                .collect(Collectors.toList());
    }

    public Supplier createSupplier(Supplier supplier) {
        log.info("Creating new supplier: {}", supplier.getName());
        if (supplier.getEmail() != null && supplierRepository.existsByEmail(supplier.getEmail())) {
            throw new IllegalArgumentException("Supplier already exists with email: " + supplier.getEmail());
        }
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
        log.info("Updating supplier with id: {}", id);
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id));
        if (updatedSupplier.getEmail() != null &&
                !updatedSupplier.getEmail().equals(existingSupplier.getEmail()) &&
                supplierRepository.existsByEmail(updatedSupplier.getEmail())) {
            throw new IllegalArgumentException("Supplier already exists with email: " + updatedSupplier.getEmail());
        }
        existingSupplier.setName(updatedSupplier.getName());
        existingSupplier.setContactPerson(updatedSupplier.getContactPerson());
        existingSupplier.setPhone(updatedSupplier.getPhone());
        existingSupplier.setEmail(updatedSupplier.getEmail());
        existingSupplier.setAddress(updatedSupplier.getAddress());
        return supplierRepository.save(existingSupplier);
    }

    public void deleteSupplier(Long id) {
        log.info("Deleting supplier with id: {}", id);
        if (!supplierRepository.existsById(id)) {
            throw new IllegalArgumentException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }
}