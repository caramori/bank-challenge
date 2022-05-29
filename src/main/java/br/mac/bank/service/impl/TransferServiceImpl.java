package br.mac.bank.service.impl;

import br.mac.bank.api.exception.MaxTransactionsExceededException;
import br.mac.bank.api.exception.SameOriginDestinationException;
import br.mac.bank.config.DateUtils;
import br.mac.bank.fixer.FixerClient;
import br.mac.bank.api.exception.InsufficientFundsException;
import br.mac.bank.model.Transfer;
import br.mac.bank.model.User;
import br.mac.bank.repositories.TransferRepo;
import br.mac.bank.service.AccountService;
import br.mac.bank.service.TaxService;
import br.mac.bank.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransferServiceImpl implements TransferService {

    private final TransferRepo transferRepo;
    private final AccountService accountService;
    private final TaxService taxService;
    private final FixerClient fixerClient;
    private final DateUtils dateUtils;

    @Override
    public int getCountTransfersToday(User user) {
        return transferRepo.getCountTransactions(user, dateUtils.today());
    }

    @Override
    public Transfer processTransfer(Transfer transfer) {

        setTaxCollected(transfer);

        validateTransfer(transfer);

        subtractAmountOrigin(transfer);
        addAmountDestination(transfer);

        setCADReturn(transfer);

        transfer.setUuid(UUID.randomUUID().toString());
        transfer.setDate(new Date());

        return save(transfer);
    }

    private void setCADReturn(Transfer transfer) {

        BigDecimal amountToCad =
                fixerClient.convertCurrency(transfer.getAmount(),
                                            transfer.getCurrency().getSymbol(),
                                            "CAD");
        transfer.setCAD(amountToCad);
    }

    private void validateTransfer(Transfer transfer) {

        boolean hasFunds = originAccountHasFunds(transfer);
        if (!hasFunds){
            throw new InsufficientFundsException();
        }

        int transactionsToday = this.getCountTransfersToday(transfer.getUser());
        if (transactionsToday >= MAX_TRANSACTIONS_ALLOWED){
            throw new MaxTransactionsExceededException();
        }

        if (transfer.getOrigin().equals(transfer.getDestination())){
            throw new SameOriginDestinationException();
        }
    }

    @Override
    public Transfer save(Transfer transfer) {
        return transferRepo.save(transfer);
    }

    private void setTaxCollected(Transfer transfer) {

        BigDecimal taxPercentage = taxService.getTaxByAmount(transfer.getAmount()).getTaxPercent();
        taxPercentage = taxPercentage.divide(new BigDecimal(100));
        BigDecimal taxCharged = transfer.getAmount().multiply(taxPercentage);
        transfer.setTaxCollected(taxCharged);
    }

    private BigDecimal getAmountWithTaxes(Transfer transfer){
        BigDecimal amount = transfer.getAmount();
        amount = amount.add(transfer.getTaxCollected());
        return amount;
    }

    private boolean originAccountHasFunds(Transfer transfer) {
        BigDecimal totalAmount = this.getAmountWithTaxes(transfer);
        BigDecimal balance = transfer.getOrigin().getBalance();
        return totalAmount.compareTo(balance) <= 0;
    }

    private void addAmountDestination(Transfer transfer) {
        BigDecimal balanceDest = transfer.getDestination().getBalance();
        balanceDest = balanceDest.add(transfer.getAmount());
        transfer.getDestination().setBalance(balanceDest);
        accountService.save(transfer.getDestination());
    }

    private void subtractAmountOrigin(Transfer transfer) {
        BigDecimal balanceOrigin = transfer.getOrigin().getBalance();
        balanceOrigin = balanceOrigin.subtract(this.getAmountWithTaxes(transfer));
        transfer.getOrigin().setBalance(balanceOrigin);
        accountService.save(transfer.getOrigin());
    }
}
