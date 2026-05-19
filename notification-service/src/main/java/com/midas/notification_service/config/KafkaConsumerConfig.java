package com.midas.notification_service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import org.apache.kafka.common.TopicPartition;

@Configuration
public class KafkaConsumerConfig {

    // Hatalı mesajı "wallet-transactions.DLQ" kuyruğuna gönderen kurtarıcı
    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> template) {
        // 3 kere tekrar dene (Retry), sonra DLQ'ya at
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template,
                (record, ex) -> new TopicPartition(record.topic() + ".DLQ", record.partition()));
        // Her 2 saniyede bir 3 kere tekrar dene
        return new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3));
    }
}
