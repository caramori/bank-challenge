package br.mac.bank.repositories;

import br.mac.bank.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepo extends JpaRepository<Currency, Long> {

    Currency findBySymbol(String symbol);

}
