package com.inventory.dto;

import com.inventory.model.Supplier;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Supplier — includes productCount computed by the service layer
 */
@Data
@NoArgsConstructor
public class SupplierDTO {

    private Long id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long productCount;

    public static SupplierDTO from(Supplier s, long productCount) {
        SupplierDTO dto = new SupplierDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setContactPerson(s.getContactPerson());
        dto.setPhone(s.getPhone());
        dto.setEmail(s.getEmail());
        dto.setAddress(s.getAddress());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setUpdatedAt(s.getUpdatedAt());
        dto.setProductCount(productCount);
        return dto;
    }
}
