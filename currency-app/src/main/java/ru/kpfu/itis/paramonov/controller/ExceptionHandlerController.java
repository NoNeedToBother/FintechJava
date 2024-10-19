package ru.kpfu.itis.paramonov.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.kpfu.itis.paramonov.dto.ErrorResponseDto;
import ru.kpfu.itis.paramonov.exception.CurrencyApiGatewayErrorException;
import ru.kpfu.itis.paramonov.exception.CurrencyCodeNotFoundException;
import ru.kpfu.itis.paramonov.validation.ValidCurrency;

import java.util.Optional;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationFail(ConstraintViolationException e) {
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            Object annotation = violation.getConstraintDescriptor().getAnnotation();
            if (annotation instanceof ValidCurrency) {
                return new ResponseEntity<>(
                        new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), ((ValidCurrency) annotation).message()),
                        HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(
                new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "Invalid parameters"),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CurrencyCodeNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCurrencyCodeNotFound(CurrencyCodeNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(CurrencyApiGatewayErrorException.class)
    public ResponseEntity<ErrorResponseDto> handleCurrencyApiGatewayFail() {
        var headers = new HttpHeaders();
        headers.add("Retry-After", "3600");
        return new ResponseEntity<>(
                new ErrorResponseDto(HttpStatus.SERVICE_UNAVAILABLE.value(), "Currency API unavailable"),
                headers,
                HttpStatus.SERVICE_UNAVAILABLE
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
