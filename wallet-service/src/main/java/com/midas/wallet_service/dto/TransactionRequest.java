package com.midas.wallet_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequest {
    private String userEmail;
    private BigDecimal amount;
}
