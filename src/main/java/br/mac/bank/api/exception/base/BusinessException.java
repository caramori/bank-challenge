package br.mac.bank.api.exception.base;

public abstract class BusinessException extends RuntimeException {

    public abstract BusinessExceptionType getType();

    @Override
    public String getMessage() {
        return getType().getMessage();
    }
}
