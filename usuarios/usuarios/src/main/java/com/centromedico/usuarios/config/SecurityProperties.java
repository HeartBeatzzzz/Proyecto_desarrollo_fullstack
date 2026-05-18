package com.centromedico.usuarios.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private final Credentials admin = new Credentials();
    private final Credentials user = new Credentials();

    @Getter
    @Setter
    public static class Credentials {

        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }
}
