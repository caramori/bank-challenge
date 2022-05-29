package br.mac.bank.service;

import br.mac.bank.model.Account;

public interface AccountService {

    Account findById(Long id);
    Account save(Account account);
}
