package com.midas.notification_service.service;

import com.midas.common.dto.MoneyTransferredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class KafkaConsumerService {
    // "wallet-transactions" topic'ini dinlemeye alıyoruz
    @KafkaListener(topics = "wallet-transactions", groupId = "midas-notification-group")
    public void consumeWalletEvent(MoneyTransferredEvent event) {
        log.info("🔔 YENİ BİLDİRİM: Midas hesabında hareket algılandı!");

        /*
        // --- DLQ'ya düsmesi icin bilincli bir deneme yapıldı!
        // Eğer gelen tutar 999 TL ise, servis bilerek çöksün (RuntimeException fırlatsın)
        if (event.getAmount().compareTo(new BigDecimal("999")) == 0) {
            log.error("💣 SERVİS ÇÖKÜYOR! Hatalı mesaj yakalandı: {}", event.getAmount());
            throw new RuntimeException("Kritik İşlem Hatası! Bu mesaj işlenemez.");
        }
        // --- DENEME BITIS ---
         */

        log.info("İşlem Detayı -> Kullanıcı: {}, Tip: {}, Tutar: {} TL",
                event.getUserEmail(), event.getTransactionType(), event.getAmount());

        // Burada SMS atma veya push notification gönderme kodları tetiklenir.
    }
}
