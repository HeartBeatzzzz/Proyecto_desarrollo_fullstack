package com.centromedico.administracionusuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AdministracionUsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdministracionUsuariosApplication.class, args);
    }
}
