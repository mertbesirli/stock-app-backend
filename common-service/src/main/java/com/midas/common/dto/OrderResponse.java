package com.midas.common.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderResponse {
    private OrderRequest request;
    private String status; // "SUCCESS" veya "FAIL"
}
