package ru.kpfu.itis.paramonov.exception;

public class CurrencyApiGatewayErrorException extends RuntimeException {
    public CurrencyApiGatewayErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
