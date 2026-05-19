package com.midas.wallet_service.controller;

import com.midas.common.dto.OrderRequest;
import com.midas.wallet_service.dto.TransactionRequest;
import com.midas.wallet_service.model.Account;
import com.midas.wallet_service.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // React uygulamamız lokalde bağlanabilsin diye CORS izni verdik
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/account")
    public ResponseEntity<Account> getAccount(@RequestParam String email) {
        return ResponseEntity.ok(walletService.getOrCreateAccount(email));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(walletService.deposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(walletService.withdraw(request));
    }

    @PostMapping("/order")
    public String createOrder(@RequestBody OrderRequest req) {
        walletService.placeOrder(req);
        return "Emir iletildi!";
    }
}
