package com.inventory.controller;

import com.inventory.dto.ApiResponse;
import com.inventory.dto.SupplierDTO;
import com.inventory.model.Supplier;
import com.inventory.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SupplierDTO>>> getAllSuppliers() {
        try {
            List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
            return ResponseEntity.ok(ApiResponse.success(suppliers));
        } catch (Exception e) {
            log.error("Error fetching suppliers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch suppliers"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Supplier>> getSupplierById(@PathVariable Long id) {
        return supplierService.getSupplierById(id)
                .map(s -> ResponseEntity.ok(ApiResponse.success(s)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Supplier not found")));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SupplierDTO>>> searchSuppliers(@RequestParam String name) {
        try {
            List<SupplierDTO> suppliers = supplierService.searchSuppliers(name);
            return ResponseEntity.ok(ApiResponse.success(suppliers));
        } catch (Exception e) {
            log.error("Error searching suppliers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to search suppliers"));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> createSupplier(@Valid @RequestBody Supplier supplier) {
        try {
            Supplier created = supplierService.createSupplier(supplier);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating supplier", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create supplier"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> updateSupplier(@PathVariable Long id,
            @Valid @RequestBody Supplier supplier) {
        try {
            Supplier updated = supplierService.updateSupplier(id, supplier);
            return ResponseEntity.ok(ApiResponse.success(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating supplier", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update supplier"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting supplier", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete supplier"));
        }
    }
}