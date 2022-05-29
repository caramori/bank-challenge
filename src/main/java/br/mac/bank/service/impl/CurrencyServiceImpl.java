package br.mac.bank.service.impl;

import br.mac.bank.model.Currency;
import br.mac.bank.repositories.CurrencyRepo;
import br.mac.bank.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepo currencyRepo;

    @Override
    public Currency save(Currency currency) {
        return currencyRepo.save(currency);
    }

    @Override
    public Currency findBySymbol(String symbol) {
        return currencyRepo.findBySymbol(symbol);
    }
}
