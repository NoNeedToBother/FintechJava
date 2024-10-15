package ru.kpfu.itis.paramonov.exception;

public class CurrencyCodeNotFoundException extends RuntimeException {

    public CurrencyCodeNotFoundException(String message) {
        super(message);
    }
}
