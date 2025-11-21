package com.inventory_backend.inventory_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.scheduling.annotation.EnableAsync;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "customer_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal originalAmount;
    private BigDecimal amountRemaining;

    private LocalDate paymentDate;

    private String remarks;
}
