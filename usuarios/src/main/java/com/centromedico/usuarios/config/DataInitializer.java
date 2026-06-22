package com.centromedico.usuarios.config;

import com.centromedico.usuarios.model.Ciudad;
import com.centromedico.usuarios.model.Usuario;
import com.centromedico.usuarios.repository.CiudadRepository;
import com.centromedico.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final int CiudadesIniciales = 2;
    private static final int UsuariosIniciales = 10;

    private final UsuarioRepository usuarioRepository;
    private final CiudadRepository ciudadRepository;
    private final Faker faker;

    @Override
    public void run(String... args) {

        if (usuarioRepository.count() > 0) {
            log.info(">>> Usuarios ya cargados. Se omite inicializacion.");
            return;
        }

        List<Ciudad> ciudades = asegurarCiudades();
        List<Usuario> usuarios = generarUsuarios(ciudades);

        usuarioRepository.saveAll(usuarios);

        log.info(">>> {} usuarios cargados correctamente con datafaker.", usuarios.size());
    }

    private List<Ciudad> asegurarCiudades() {
        List<Ciudad> ciudades = ciudadRepository.findAll();
        if (!ciudades.isEmpty()) {
            return ciudades;
        }

        log.info(">>> Cargando ciudades iniciales con datafaker...");

        List<Ciudad> nuevasCiudades = new ArrayList<>();
        for (int i = 0; i < CiudadesIniciales; i++) {
            nuevasCiudades.add(new Ciudad(
                    null,
                    normalizarTexto(faker.address().city()),
                    normalizarTexto(faker.address().cityName())
            ));
        }

        return ciudadRepository.saveAll(nuevasCiudades);
    }

    private List<Usuario> generarUsuarios(List<Ciudad> ciudades) {
        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < UsuariosIniciales; i++) {
            Ciudad ciudad = ciudades.get(faker.number().numberBetween(0, ciudades.size()));
            usuarios.add(new Usuario(
                    null,
                    faker.number().digits(8) + "-" + faker.number().randomDigit(),
                    null,
                    normalizarTexto(faker.name().fullName()),
                    faker.timeAndDate().birthday(18, 99),
                    ciudad,
                    "+569" + faker.number().digits(8),
                    faker.internet().safeEmailAddress().toLowerCase(Locale.ROOT),
                    true
            ));
        }

        return usuarios;
    }

    private String normalizarTexto(String valor) {
        return valor == null ? null : valor.trim().replaceAll("\\s+", " ");
    }
}
