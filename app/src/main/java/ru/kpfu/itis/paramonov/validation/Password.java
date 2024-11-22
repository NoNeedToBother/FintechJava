package ru.kpfu.itis.paramonov.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=PasswordConstraintValidator.class)
public @interface Password {
    String message() default "Weak password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
