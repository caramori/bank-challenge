package br.mac.bank.repositories;

import br.mac.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Long> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Account> findById(Long aLong);
}
