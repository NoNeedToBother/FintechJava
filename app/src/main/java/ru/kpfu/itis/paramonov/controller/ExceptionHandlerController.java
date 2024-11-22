package ru.kpfu.itis.paramonov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.kpfu.itis.paramonov.dto.responses.ErrorResponseDto;
import ru.kpfu.itis.paramonov.exception.*;

import java.util.Optional;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEventNotFound(EventNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(PlaceNotExistsException.class)
    public ResponseEntity<ErrorResponseDto> handlePlaceNotExists(PlaceNotExistsException e) {
        return new ResponseEntity<>(
                new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePlaceNotFound(PlaceNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCredentials(InvalidCredentialsException e) {
        return new ResponseEntity<>(
                new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCode(InvalidCodeException e) {
        return new ResponseEntity<>(
                new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Optional<ObjectError> error =
                e.getBindingResult().getAllErrors().stream().findFirst();
        if (error.isPresent()) {
            String message = error.get().getDefaultMessage();
            return new ResponseEntity<>(
                    new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), message),
                    HttpStatus.BAD_REQUEST
            );
        } else {
            return new ResponseEntity<>(
                    new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "Invalid arguments"),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
