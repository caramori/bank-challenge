package br.mac.bank.service;

import br.mac.bank.model.Currency;

public interface CurrencyService {

    Currency save(Currency currency);
    Currency findBySymbol(String symbol);
}
