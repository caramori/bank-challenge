package br.mac.bank.service;

import br.mac.bank.model.Tax;

import java.math.BigDecimal;

public interface TaxService {

    Tax save(Tax tax);
    Tax getTaxByAmount(BigDecimal amount);
}
