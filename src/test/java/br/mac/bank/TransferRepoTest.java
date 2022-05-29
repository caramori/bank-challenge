package br.mac.bank;

import br.mac.bank.model.*;
import br.mac.bank.repositories.*;
import br.mac.bank.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@Import(UserServiceImpl.class)
public class TransferRepoTest {

    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private TransferRepo transferRepo;
    @Autowired private AccountRepo accountRepo;
    @Autowired private UserRepo userRepo;
    @Autowired private CurrencyRepo currencyRepo;

    @Test
    public void shouldReturnCorrectTransfersCount(){

        transferRepo.deleteAll();

        Date date = new Date();
        final int QTD_TRANSFERS = 2;

        User user = new User(null, "Test1", "test1", "TEST1", null);
        userRepo.save(user);
        User user2 = new User(null, "Test2", "test2", "TEST2", null);
        userRepo.save(user2);


        Account account = new Account(null, new BigDecimal(10000), null);
        accountRepo.save(account);
        Account account2 = new Account(null, new BigDecimal(10000), null);
        accountRepo.save(account2);

        Currency curr = new Currency(null, "USD", "USD");
        currencyRepo.save(curr);

        for (User userTransfer : new User[]{user, user2}){
            for (int i = 0; i< QTD_TRANSFERS; i++){
                transferRepo.save(new Transfer(null, null, date, account, account2,
                        new BigDecimal(1000), curr, "", BigDecimal.ZERO, BigDecimal.ZERO, userTransfer));
            }
        }

        Assertions.assertEquals(transferRepo.getCountTransactions(user, date), QTD_TRANSFERS);
    }
}
