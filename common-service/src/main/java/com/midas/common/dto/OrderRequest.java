package com.midas.common.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderRequest {
    private String userEmail;
    private String symbol; // Örn: "AAPL"
    private BigDecimal amount;
    private Long transactionId; // İşlemleri takip etmek için UUID/Long
}
