package com.inventory.dto;

import com.inventory.model.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Category — includes productCount computed by the service layer
 */
@Data
@NoArgsConstructor
public class CategoryDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long productCount;

    public static CategoryDTO from(Category c, long productCount) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setDescription(c.getDescription());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        dto.setProductCount(productCount);
        return dto;
    }
}
