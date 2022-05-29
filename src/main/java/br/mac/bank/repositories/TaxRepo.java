package br.mac.bank.repositories;

import br.mac.bank.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TaxRepo extends JpaRepository<Tax, Long> {

    @Query("SELECT t FROM Tax t WHERE :amount > t.minAmount and (:amount <= t.maxAmount or t.maxAmount IS NULL)")
    public Tax getTaxByAmount(@Param("amount")BigDecimal amount);

}
