package ru.kpfu.itis.paramonov.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;

public class CurrencyConstraintValidator implements ConstraintValidator<ValidCurrency, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Currency.getInstance(s.toUpperCase());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
