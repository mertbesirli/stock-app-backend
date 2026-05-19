package com.midas.wallet_service.service;

import com.midas.common.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import com.midas.common.dto.MoneyTransferredEvent;

@Service
@RequiredArgsConstructor
@Slf4j // Konsola log basma lombok
public class KafkaProducerService {

    // Spring Boot'un bize sunduğu hazır Kafka şablonu
    private final KafkaTemplate<String, MoneyTransferredEvent> kafkaTemplateNotification;

    private final KafkaTemplate<String, OrderRequest> kafkaTemplateStock;

    private static final String TOPIC_NOTIFICATION = "wallet-transactions";
    private static final String TOPIC_ORDER= "order-topic";

    public void sendTransactionEvent(MoneyTransferredEvent event) {
        log.info("Kafka'ya event fırlatılıyor... Kullanıcı: {}, Tip: {}, Tutar: {}",
                event.getUserEmail(), event.getTransactionType(), event.getAmount());

        kafkaTemplateNotification.send(TOPIC_NOTIFICATION, event.getUserEmail(), event);
    }

    public void sendOrderStartedEvent(OrderRequest request) {
        log.info("Saga akışı başlıyor: Emir alındı, stok servisi bekleniyor...");
        kafkaTemplateStock.send(TOPIC_ORDER, request.getUserEmail(), request);
    }
}
