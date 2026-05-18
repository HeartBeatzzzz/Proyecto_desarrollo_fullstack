package com.parcialdos.empleados.config;

import com.parcialdos.empleados.Model.Empleados;
import com.parcialdos.empleados.Repository.EmpleadoRepository;
import com.parcialdos.empleados.Service.EmpleadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final EmpleadoRepository Repo;
    private final EmpleadoService Servy;
    @Override
    public void run(String... args) {

        if (Repo.count() > 0) {
            log.info("El repositorio ya posee empleados.");
            return;
        }

        log.info("Cargando BD de empleados...");
        Repo.saveAll(List.of(
                new Empleados(null,"4444444CUÁ","Señor Manguera","Jefe televisivo",1L,"No.","SeñorManguera@aplaplac.cl"),
                new Empleados(null,"15032003-1","Tulio Triviño","Papanatas",2L,"Tampoco.","TulioTriviño@aplaplac.cl"),
                new Empleados(null,"10011991-K","Dr.Nick Riviera","Carnicero",2L,"9171219899","NickRiviera@Yahoo.com")                ));
    }
}
