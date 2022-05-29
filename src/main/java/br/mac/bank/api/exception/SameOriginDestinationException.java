package br.mac.bank.api.exception;

import br.mac.bank.api.exception.base.BusinessException;
import br.mac.bank.api.exception.base.BusinessExceptionType;

public class SameOriginDestinationException extends BusinessException {
    @Override
    public BusinessExceptionType getType() {
        return BusinessExceptionType.SAME_ORIGIN_DESTINATION;
    }
}
