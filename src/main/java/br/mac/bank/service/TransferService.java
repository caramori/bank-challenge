package br.mac.bank.service;

import br.mac.bank.model.Transfer;
import br.mac.bank.model.User;

public interface TransferService {

    public static final int MAX_TRANSACTIONS_ALLOWED = 3;

    int getCountTransfersToday(User user);
    Transfer processTransfer(Transfer transfer);
    Transfer save(Transfer transfer);

}
