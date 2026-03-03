package com.inventory.controller;

import com.inventory.dto.ApiResponse;
import com.inventory.dto.CategoryDTO;
import com.inventory.model.Category;
import com.inventory.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(ApiResponse.success(categories));
        } catch (Exception e) {
            log.error("Failed to fetch categories", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch categories"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        try {
            return categoryService.getCategoryById(id)
                    .map(category -> ResponseEntity.ok(ApiResponse.success(category)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Category not found")));
        } catch (Exception e) {
            log.error("Failed to fetch category with id: {}", id, e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch category"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> searchCategories(@RequestParam String name) {
        try {
            List<CategoryDTO> categories = categoryService.searchCategories(name);
            return ResponseEntity.ok(ApiResponse.success(categories));
        } catch (Exception e) {
            log.error("Failed to search categories", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to search categories"));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody Category category) {
        try {
            Category createdCategory = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Category created successfully", createdCategory));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id,
            @Valid @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(ApiResponse.success("Category updated successfully", updatedCategory));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}