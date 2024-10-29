package ru.kpfu.itis.paramonov.exception;

public class PlaceNotFoundException extends RuntimeException {
    public PlaceNotFoundException(String message) {
        super(message);
    }
}
