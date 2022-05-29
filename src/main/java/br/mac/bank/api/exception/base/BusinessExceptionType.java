package br.mac.bank.api.exception.base;

public enum BusinessExceptionType {

    INSUFFICIENT_FUNDS(456, "insufficient-funds"),
    TRANSACTIONS_EXCEEDED(457, "User exceeded limit of transactions by day (3 transactions)"),
    SAME_ORIGIN_DESTINATION(458, "Origin and destination account can't be the same");
    private int code;
    private String message;

    private BusinessExceptionType(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}