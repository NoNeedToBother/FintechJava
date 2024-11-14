package ru.kpfu.itis.paramonov.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth")
@Getter
@Setter
public class AuthConfigurationsProperties {

    private PasswordConfig passwordConfig;

    @Configuration
    @ConfigurationProperties(prefix = "auth.password")
    @Getter
    @Setter
    public static class PasswordConfig {

        private int minLength;

        private boolean requireDigit;

        private boolean requireLowercase;

        private boolean requireUppercase;
    }
}
