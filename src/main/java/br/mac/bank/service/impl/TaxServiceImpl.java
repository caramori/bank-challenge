package br.mac.bank.service.impl;

import br.mac.bank.model.Tax;
import br.mac.bank.repositories.TaxRepo;
import br.mac.bank.service.TaxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TaxServiceImpl implements TaxService {


    private final TaxRepo taxRepo;

    @Override
    public Tax save(Tax tax) {
        return taxRepo.save(tax);
    }

    @Override
    public Tax getTaxByAmount(BigDecimal amount) {
        return taxRepo.getTaxByAmount(amount);
    }
}
