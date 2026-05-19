package com.midas.wallet_service.service;

import com.midas.common.dto.OrderRequest;
import com.midas.wallet_service.dto.TransactionRequest;
import com.midas.wallet_service.model.Account;
import com.midas.wallet_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.midas.common.dto.MoneyTransferredEvent;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final AccountRepository accountRepository;
    private final KafkaProducerService kafkaProducerService;

    // Kullanıcının mevcut durumunu getir (Eğer kullanıcı yoksa demo amaçlı otomatik hesap açalım)
    @Transactional
    public Account getOrCreateAccount(String userEmail) {
        return accountRepository.findByUserEmail(userEmail)
                .orElseGet(() -> accountRepository.save(
                        Account.builder()
                                .userEmail(userEmail)
                                .balance(BigDecimal.ZERO) // Yeni hesaba 0 TL bakiye
                                .lockedBalance(BigDecimal.ZERO)
                                .build()
                ));
    }
    // Para yatırma islemi
    @Transactional
    public Account deposit(TransactionRequest request){
        Account account = getOrCreateAccount(request.getUserEmail());

        // Mevcut bakiyeye ekle
        BigDecimal newBalance = account.getBalance().add(request.getAmount());
        account.setBalance(newBalance);
        Account savedAccount = accountRepository.save(account);

        // kafka send event area
        MoneyTransferredEvent event = MoneyTransferredEvent.builder()
                .userEmail(request.getUserEmail())
                .amount(request.getAmount())
                .transactionType("DEPOSIT")
                .timestamp(System.currentTimeMillis())
                .build();
        kafkaProducerService.sendTransactionEvent(event);
        // ------

        return savedAccount;
    }

    //Para cekme islemi
    @Transactional
    public Account withdraw(TransactionRequest request){
        // Artik veriyi kilitli okuyoruz. Başka hiçbir thread bu satıra dokunamaz!
        Account account = accountRepository.findByUserEmailWithLock(request.getUserEmail())
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadı"));

        // Bakiyenin çekilmek istenen tutardan küçük olup olmadığının kontrolü
        // amount > balance ise compareTo sonucu 1 döner.
        if(account.getBalance().compareTo(request.getAmount()) < 0){
            throw new RuntimeException("Yetersiz bakiye! İşlem iptal edildi.");
        }
        // bakiyeyi düsür
        BigDecimal newBalance = account.getBalance().subtract(request.getAmount());
        account.setBalance(newBalance);
        Account savedAccount = accountRepository.save(account);

        // kafka send event area
        MoneyTransferredEvent event = MoneyTransferredEvent.builder()
                .userEmail(request.getUserEmail())
                .amount(request.getAmount())
                .transactionType("WITHDRAW")
                .timestamp(System.currentTimeMillis())
                .build();

        kafkaProducerService.sendTransactionEvent(event);
        // ------

        return savedAccount;
    }

    @Transactional
    public void placeOrder(OrderRequest request){
        // 1. Bakiyeyi kontrol et ve bloke et
        Account account = accountRepository.findByUserEmailWithLock(request.getUserEmail())
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadı"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Yetersiz bakiye!");
        }

        // Bakiyeyi blokeye al
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        account.setLockedBalance(account.getLockedBalance().add(request.getAmount()));
        accountRepository.save(account);

        // 2. Stock Service'e mesaj gönder (Kafka aracılığıyla)
        kafkaProducerService.sendOrderStartedEvent(request);
    }
}
