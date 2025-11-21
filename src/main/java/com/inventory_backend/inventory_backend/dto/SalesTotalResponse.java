package com.inventory_backend.inventory_backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SalesTotalResponse {

    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal grandTotal;
    private boolean outOfStock;
    private List<String> outOfStockItems;

    public SalesTotalResponse(BigDecimal total, BigDecimal discount, BigDecimal grandTotal) {

        this.totalAmount=total;
        this.discount=discount;
        this.grandTotal=grandTotal;
    }
}
