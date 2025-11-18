package com.inventory_backend.inventory_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String invoiceNo;

    private LocalDate invoiceDate;

    private Double totalAmount;
    private Double discount;
    private Double grandTotal;

    private LocalDateTime createdAt = LocalDateTime.now();
}
