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

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login/usuario", "/login/admin", "/perform_login", "/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        .requestMatchers("/api/usuarios/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/usuarios/*/citas/**", "/api/usuarios/*").hasAnyRole("USER", "ADMIN")
                        .anyRequest().permitAll())
                .exceptionHandling(exception -> exception
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login/admin"),
                                PathPatternRequestMatcher.pathPattern("/api/usuarios/admin/**"))
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login/usuario"),
                                PathPatternRequestMatcher.pathPattern("/api/usuarios/{usuarioId}/citas/**"))
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login/usuario"),
                                PathPatternRequestMatcher.pathPattern("/api/usuarios/{id}")))
                .formLogin(form -> form
                        .loginPage("/login/usuario")
                        .loginProcessingUrl("/perform_login")
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(SecurityProperties securityProperties) {
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
