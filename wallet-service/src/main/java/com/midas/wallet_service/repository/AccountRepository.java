package com.midas.wallet_service.repository;

import com.midas.wallet_service.model.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // Sadece bu kullanıcıya ait satırı veritabanı seviyesinde (SELECT ... FOR UPDATE) kilitle diyoruz
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.userEmail = :userEmail")
    Optional<Account> findByUserEmailWithLock(String userEmail);

    // Normal non-lock sorgu (Sadece bakiye görüntülemek için kullanılabilir)
    Optional<Account> findByUserEmail(String userEmail);
}
