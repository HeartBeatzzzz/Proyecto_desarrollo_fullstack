package com.centromedico.usuarios.config;

import com.centromedico.usuarios.model.Usuario;
import com.centromedico.usuarios.repository.CiudadRepository;
import com.centromedico.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CiudadRepository ciudadRepository;

    @Override
    public void run(String... args) {

        if (usuarioRepository.count() > 0) {
            log.info(">>> Usuarios ya cargados. Se omite inicialización.");
            return;
        }

        log.info(">>> Cargando usuarios iniciales...");

        // Se usa orElseThrow para fallar de forma explícita si la ciudad no existe.
        usuarioRepository.saveAll(List.of(
                new Usuario(null, "12345678-9", null, "Juan Pérez",
                        LocalDate.of(1995, 5, 12),
                        ciudadRepository.findById(1L)
                                .orElseThrow(() -> new RuntimeException("Ciudad ID 1 no encontrada")),
                        "+56911111111", "juan.perez@gmail.com"),

                new Usuario(null, "98765432-1", null, "María González",
                        LocalDate.of(1988, 10, 3),
                        ciudadRepository.findById(2L)
                                .orElseThrow(() -> new RuntimeException("Ciudad ID 2 no encontrada")),
                        "+56922222222", "maria.gonzalez@gmail.com"),

                new Usuario(null, "11222333-4", null, "Carlos Rojas",
                        LocalDate.of(2000, 1, 25),
                        ciudadRepository.findById(1L)
                                .orElseThrow(() -> new RuntimeException("Ciudad ID 1 no encontrada")),
                        "+56933333333", "carlos.rojas@gmail.com")
        ));

        log.info(">>> 3 usuarios cargados correctamente.");
    }
}
