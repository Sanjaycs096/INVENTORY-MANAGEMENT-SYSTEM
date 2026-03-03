package com.inventory.service;

import com.inventory.dto.CategoryDTO;
import com.inventory.model.Category;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<CategoryDTO> getAllCategories() {
        log.info("Fetching all categories");
        return categoryRepository.findAll().stream()
                .map(c -> CategoryDTO.from(c, productRepository.countByCategoryId(c.getId())))
                .collect(Collectors.toList());
    }

    public Optional<Category> getCategoryById(Long id) {
        log.info("Fetching category with id: {}", id);
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryByName(String name) {
        log.info("Fetching category with name: {}", name);
        return categoryRepository.findByName(name);
    }

    public List<CategoryDTO> searchCategories(String name) {
        log.info("Searching categories with name containing: {}", name);
        return categoryRepository.findByNameContainingIgnoreCase(name).stream()
                .map(c -> CategoryDTO.from(c, productRepository.countByCategoryId(c.getId())))
                .collect(Collectors.toList());
    }

    public Category createCategory(Category category) {
        log.info("Creating new category: {}", category.getName());
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        log.info("Updating category with id: {}", id);
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
        if (!existingCategory.getName().equals(updatedCategory.getName()) &&
                categoryRepository.existsByName(updatedCategory.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + updatedCategory.getName());
        }
        existingCategory.setName(updatedCategory.getName());
        existingCategory.setDescription(updatedCategory.getDescription());
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}", id);
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}