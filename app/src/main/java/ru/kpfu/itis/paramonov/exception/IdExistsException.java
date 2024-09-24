package ru.kpfu.itis.paramonov.exception;

public class IdExistsException extends RuntimeException {

    public IdExistsException(String message) {
        super(message);
    }
}
