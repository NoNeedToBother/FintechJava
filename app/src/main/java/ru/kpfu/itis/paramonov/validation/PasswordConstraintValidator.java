package ru.kpfu.itis.paramonov.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kpfu.itis.paramonov.config.AuthConfigurationsProperties;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {

    @Autowired
    private AuthConfigurationsProperties authConfig;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null) {
            //Handled by @NotNull
            return true;
        }
        var passwordConfig = authConfig.getPasswordConfig();
        if (password.length() < passwordConfig.getMinLength()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate
                            ("Password should have length more then or equal to 8")
                    .addPropertyNode("symbols")
                    .addConstraintViolation();
            return false;
        } else {
            var passedDigitCheck = !passwordConfig.isRequireDigit();
            var passedLowercaseCheck = !passwordConfig.isRequireLowercase();
            var passedUppercaseCheck = !passwordConfig.isRequireUppercase();
            for (Character c : password.toCharArray()) {
                if (Character.isDigit(c)) passedDigitCheck = true;
                if (Character.isLowerCase(c)) passedLowercaseCheck = true;
                if (Character.isUpperCase(c)) passedUppercaseCheck = true;
            }
            if (!(passedDigitCheck && passedLowercaseCheck && passedUppercaseCheck)) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate
                                ("Password should have at least one of each: uppercase, lowercase and digit character")
                        .addPropertyNode("symbols")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
