package com.inventory.dto;

import com.inventory.model.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Transaction — provides flat fields for the frontend
 */
@Data
@NoArgsConstructor
public class TransactionDTO {

    private Long id;
    private String transactionCode;
    private String transactionType; // "IN", "OUT", "ADJUSTMENT", "RETURN"
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private Integer previousQuantity;
    private Integer newQuantity;
    private String notes;
    private LocalDateTime transactionDate;

    // Flat product fields
    private Long productId;
    private String productName;

    // Flat user fields
    private Long userId;
    private String userName;

    /**
     * Build a TransactionDTO from a Transaction entity.
     */
    public static TransactionDTO from(Transaction t) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(t.getId());
        dto.setTransactionCode(t.getTransactionCode());
        dto.setQuantity(t.getQuantity());
        dto.setUnitPrice(t.getUnitPrice());
        dto.setTotalAmount(t.getTotalAmount());
        dto.setPreviousQuantity(t.getPreviousQuantity());
        dto.setNewQuantity(t.getNewQuantity());
        dto.setNotes(t.getNotes());
        dto.setTransactionDate(t.getTransactionDate());

        // Map enum → simplified string key used by the frontend
        if (t.getTransactionType() != null) {
            switch (t.getTransactionType()) {
                case STOCK_IN -> dto.setTransactionType("IN");
                case STOCK_OUT -> dto.setTransactionType("OUT");
                case ADJUSTMENT -> dto.setTransactionType("ADJUSTMENT");
                case RETURN -> dto.setTransactionType("RETURN");
                default -> dto.setTransactionType(t.getTransactionType().name());
            }
        }

        if (t.getProduct() != null) {
            dto.setProductId(t.getProduct().getId());
            dto.setProductName(t.getProduct().getName());
        }

        if (t.getUser() != null) {
            dto.setUserId(t.getUser().getId());
            dto.setUserName(t.getUser().getUsername());
        }

        return dto;
    }
}
