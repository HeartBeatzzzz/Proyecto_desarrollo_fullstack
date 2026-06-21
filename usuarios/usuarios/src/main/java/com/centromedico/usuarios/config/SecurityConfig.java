package com.centromedico.usuarios.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration  // Indica que es una clase de configuración de Spring
@EnableWebSecurity // Activa la seguridad web de Spring Security
@EnableConfigurationProperties(SecurityProperties.class)

/*
 Configuración de Spring Security para el microservicio de usuarios.
 Define reglas de autorización, autenticación (formulario y Basic Auth),
 usuarios en memoria y puntos de entrada personalizados.*/

public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/api/usuarios/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(SecurityProperties securityProperties) {

        //DEFINIR ROLES DE USUARIO Y ADMIN
        //noop para texto simple sin cifrado complejo por ahora
        return new InMemoryUserDetailsManager(
                User.withUsername(securityProperties.getAdmin().getUsername())
                        .password("{noop}" + securityProperties.getAdmin().getPassword())
                        .roles("ADMIN", "USER")
                        .build(),
                User.withUsername(securityProperties.getUser().getUsername())
                        .password("{noop}" + securityProperties.getUser().getPassword())
                        .roles("USER")
                        .build()
        );
    }
}
