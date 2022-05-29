package br.mac.bank.repositories;

import br.mac.bank.model.Transfer;
import br.mac.bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface TransferRepo extends JpaRepository<Transfer, Long> {

    @Query(value = "SELECT count(t) FROM Transfer t "+
                   "WHERE t.user = :user "+
                    "AND cast(t.date as date) = cast(:date as date) ")
    public int getCountTransactions(@Param("user") User user, @Param("date")Date date);

}
