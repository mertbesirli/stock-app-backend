package com.midas.common.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MoneyTransferredEvent{
    private String userEmail;
    private BigDecimal amount;
    private String transactionType; // "DEPOSIT" veya "WITHDRAW"
    private Long timestamp;
}