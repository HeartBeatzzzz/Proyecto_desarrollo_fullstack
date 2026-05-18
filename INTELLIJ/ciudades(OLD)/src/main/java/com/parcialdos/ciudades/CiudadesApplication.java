package com.parcialdos.ciudades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CiudadesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CiudadesApplication.class, args);
	}

}
