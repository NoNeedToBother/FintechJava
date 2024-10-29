package ru.kpfu.itis.paramonov.exception;

public class RemoteGatewayErrorException extends RuntimeException {
    public RemoteGatewayErrorException(String message) {
        super(message);
    }
}
