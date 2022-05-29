package br.mac.bank.service.impl;

import br.mac.bank.model.Account;
import br.mac.bank.repositories.AccountRepo;
import br.mac.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;

    @Override
    public Account findById(Long id) {
        return accountRepo.findById(id).get();
    }

    @Override
    public Account save(Account account) {
        return accountRepo.save(account);
    }
}
