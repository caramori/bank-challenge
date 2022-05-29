package br.mac.bank;

import br.mac.bank.model.Tax;
import br.mac.bank.repositories.TaxRepo;
import br.mac.bank.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@Import(UserServiceImpl.class)
public class TaxRepoTest {

    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private TaxRepo taxRepo;

    @Test
    public void shouldReturnCorrectTaxes(){

        taxRepo.deleteAll();

        Tax tax2percent = taxRepo.save(new Tax(null, BigDecimal.ZERO, new BigDecimal(100), new BigDecimal(2)));
        Tax tax5percent = taxRepo.save(new Tax(null, new BigDecimal(100), null, new BigDecimal(5)));

        Tax tax = taxRepo.getTaxByAmount(new BigDecimal(99));
        Assertions.assertEquals(tax.getTaxPercent().compareTo(tax2percent.getTaxPercent()), 0);

        tax = taxRepo.getTaxByAmount(new BigDecimal(100));
        Assertions.assertEquals(tax.getTaxPercent().compareTo(tax2percent.getTaxPercent()), 0);

        tax = taxRepo.getTaxByAmount(new BigDecimal(100.1));
        Assertions.assertEquals(tax.getTaxPercent().compareTo(tax5percent.getTaxPercent()), 0);
    }
}
