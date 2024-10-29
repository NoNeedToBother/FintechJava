package ru.kpfu.itis.paramonov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.kpfu.itis.paramonov.dto.responses.BaseResponseDto;
import ru.kpfu.itis.paramonov.exception.DateFormatException;
import ru.kpfu.itis.paramonov.exception.IdExistsException;
import ru.kpfu.itis.paramonov.exception.RemoteGatewayErrorException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(RemoteGatewayErrorException.class)
    public ResponseEntity<BaseResponseDto> handleRemoteGatewayError(RemoteGatewayErrorException e) {
        return new ResponseEntity<>(
                new BaseResponseDto(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    @ExceptionHandler(IdExistsException.class)
    public ResponseEntity<BaseResponseDto> handleIdAlreadyExistsError(IdExistsException e) {
        return new ResponseEntity<>(
                new BaseResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DateFormatException.class)
    public ResponseEntity<BaseResponseDto> handleInvalidDateFormatError(DateFormatException e) {
        return new ResponseEntity<>(
                new BaseResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST
        );
    }
}
