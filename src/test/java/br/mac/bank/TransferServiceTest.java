package br.mac.bank;

import br.mac.bank.api.exception.InsufficientFundsException;
import br.mac.bank.api.exception.MaxTransactionsExceededException;
import br.mac.bank.config.DateUtils;
import br.mac.bank.fixer.FixerClient;
import br.mac.bank.model.*;
import br.mac.bank.repositories.AccountRepo;
import br.mac.bank.repositories.TransferRepo;
import br.mac.bank.service.TaxService;
import br.mac.bank.service.TransferService;
import br.mac.bank.service.impl.AccountServiceImpl;
import br.mac.bank.service.impl.CurrencyServiceImpl;
import br.mac.bank.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;


public class TransferServiceTest {

    @InjectMocks
    private TransferServiceImpl transferService;

    @Mock
    private TransferRepo transferRepo;

    @Mock
    private AccountServiceImpl accountService;

    @Mock
    private CurrencyServiceImpl currencyService;

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private TaxService taxService;

    @Mock
    private FixerClient fixerClient;

    @Mock
    private DateUtils dateUtils;


    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    class DefaultCenario {

        BigDecimal originalBalance = new BigDecimal(2000);
        BigDecimal transferAmount = new BigDecimal(1000);
        Tax tax = new Tax(1L, new BigDecimal(100), null, new BigDecimal(5));

        User user = new User(1L, "Person A", "pa", "123", null);
        Account origin = new Account(1L, originalBalance, 1L);

        Account destination = new Account(2L, new BigDecimal(0), 1L);

        Currency currency = new Currency(null, "USD", "USD");

        public DefaultCenario(){
            initMocks();
        }

        public DefaultCenario(BigDecimal transferAmount){
            this.transferAmount = transferAmount;
            initMocks();
        }

        private void initMocks(){
            when(accountRepo.findById(1L)).thenReturn(Optional.of(origin));
            when(accountRepo.findById(2L)).thenReturn(Optional.of(origin));
            when(taxService.getTaxByAmount(transferAmount)).thenReturn(tax);
            when(accountService.save(origin)).thenReturn(origin);
            when(accountService.save(destination)).thenReturn(destination);
            when(currencyService.findBySymbol("USD")).thenReturn(currency);
            when(fixerClient.convertCurrency(transferAmount, currency.getSymbol(), "CAD"))
                    .thenReturn(transferAmount);

        }
    }

    @Test
    public void shouldTransfer(){

        DefaultCenario c = new DefaultCenario();

        Transfer t = new Transfer();
        t.setCurrency(c.currency);
        t.setAmount(c.transferAmount);
        t.setOrigin(c.origin);
        t.setDestination(c.destination);

        transferService.processTransfer(t);

        Assertions.assertEquals(c.destination.getBalance().compareTo(c.transferAmount), 0,
                "Destination balance should be " + c.transferAmount.toPlainString() +
                        " but it is " + c.destination.getBalance().toPlainString());

        BigDecimal expectedOriginNewBalance = c.originalBalance.subtract(c.transferAmount);
        BigDecimal taxCharged = c.transferAmount.multiply(c.tax.getTaxPercent().divide(new BigDecimal(100)));
        expectedOriginNewBalance = expectedOriginNewBalance.subtract(taxCharged);

        Assertions.assertEquals(c.origin.getBalance().compareTo(expectedOriginNewBalance), 0,
                "New origin balance should be " + expectedOriginNewBalance);

        Assertions.assertNotNull(t.getUuid(), "UUID should be generated");
    }

    @Test
    public void shouldNotTransfer_InsufficientFunds(){

        BigDecimal transferAmount = new BigDecimal(2000);
        DefaultCenario c = new DefaultCenario(transferAmount);

        Transfer t = new Transfer();
        t.setCurrency(c.currency);
        t.setAmount(transferAmount);
        t.setOrigin(c.origin);
        t.setDestination(c.destination);

        Assertions.assertThrows(InsufficientFundsException.class, ()-> {
            transferService.processTransfer(t);
        });
    }

    @Test
    public void shouldNotTransfer_MaxTransactionsExceeded(){

        BigDecimal transferAmount = new BigDecimal(10);
        DefaultCenario c = new DefaultCenario(transferAmount);

        Transfer t = new Transfer();
        t.setCurrency(c.currency);
        t.setAmount(transferAmount);
        t.setOrigin(c.origin);
        t.setDestination(c.destination);
        t.setUser(c.user);

        Date now = new Date();
        when(dateUtils.today()).thenReturn(now);
        when(transferRepo.getCountTransactions(c.user, now))
                .thenReturn(TransferService.MAX_TRANSACTIONS_ALLOWED);

        Assertions.assertThrows(MaxTransactionsExceededException.class, ()-> {
            transferService.processTransfer(t);
        });
    }
}
