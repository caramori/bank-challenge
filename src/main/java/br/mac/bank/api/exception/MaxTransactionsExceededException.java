package br.mac.bank.api.exception;

import br.mac.bank.api.exception.base.BusinessException;
import br.mac.bank.api.exception.base.BusinessExceptionType;

public class MaxTransactionsExceededException extends BusinessException {

    @Override
    public BusinessExceptionType getType() {
        return BusinessExceptionType.TRANSACTIONS_EXCEEDED;
    }

}
