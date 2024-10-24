package ru.kpfu.itis.paramonov.exception;

public class PlaceNotExistsException extends RuntimeException {
    public PlaceNotExistsException(String message) {
        super(message);
    }
}
