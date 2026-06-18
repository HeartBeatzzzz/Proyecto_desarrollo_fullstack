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
        return http
                .csrf(AbstractHttpConfigurer::disable) //desactivar csrf para bajar complejidad de auth
                // configura las reglas de autorización por URL
                .authorizeHttpRequests(auth -> auth

                        //configuracion de los endpoints
                        .requestMatchers("/login/usuario", "/login/admin", "/perform_login", "/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll() //publicos
                        .requestMatchers("/api/usuarios/admin/**").hasRole("ADMIN") //solo admin
                        //user y admin
                        .requestMatchers("/api/usuarios/*/citas/**", "/api/usuarios/*").hasAnyRole("USER", "ADMIN")
                        //cualquier otra es publica por defecto
                        .anyRequest().permitAll())
                .exceptionHandling(exception -> exception
                        // para rutas de admin, redirige a la página de login de admin
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login/admin"),
                                PathPatternRequestMatcher.pathPattern("/api/usuarios/admin/**"))

                        // para rutas de citas, redirige a login de usuario
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login/usuario"),
                                PathPatternRequestMatcher.pathPattern("/api/usuarios/{usuarioId}/citas/**"))
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login/usuario"),
                                PathPatternRequestMatcher.pathPattern("/api/usuarios/{id}")))

                //configuración de autenticación basada en formulario (para pruebas con navegador)
                .formLogin(form -> form
                        .loginPage("/login/usuario") //pagina de login personalizada
                        .loginProcessingUrl("/perform_login") //donde se envian las credenciales
                        .permitAll())

                //habilita autenticacion http para pruebas con postman
                .httpBasic(Customizer.withDefaults())
                .build();
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
