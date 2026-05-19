package com.midas.wallet_service.service;

import com.midas.common.dto.OrderResponse;
import com.midas.wallet_service.model.Account;
import com.midas.wallet_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class SagaOrchestrator {
    private final AccountRepository accountRepository;

    @KafkaListener(topics = "order-response-topic", groupId = "wallet-saga-group")
    @Transactional
    public void handleResponse(OrderResponse response) {
        Account account = accountRepository.findByUserEmail(response.getRequest().getUserEmail()).get();
        BigDecimal amount = response.getRequest().getAmount();

        if ("FAIL".equals(response.getStatus())) {
            log.warn("Rollback başlıyor: {}", response.getRequest().getUserEmail());
            account.setLockedBalance(account.getLockedBalance().subtract(amount));
            account.setBalance(account.getBalance().add(amount));
        } else {
            log.info("İşlem onaylandı.");
            account.setLockedBalance(account.getLockedBalance().subtract(amount));
        }
        accountRepository.save(account);
    }
}
