package com.midas.stock_service.service;

import com.midas.common.dto.OrderRequest;
import com.midas.common.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockConsumerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order-topic", groupId = "stock-group")
    public void handleOrder(OrderRequest request) {
        String status = "FAIL".equals(request.getSymbol()) ? "FAIL" : "SUCCESS";
        log.warn("Istek geldi: {},", request.getUserEmail());
        kafkaTemplate.send("order-response-topic", new OrderResponse(request, status));
    }
}
