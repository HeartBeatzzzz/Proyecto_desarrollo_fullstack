package com.centromedico.usuarios.repository;

import com.centromedico.usuarios.model.Ciudad;
import com.centromedico.usuarios.model.Usuario;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;


import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UsuarioRepository repository;

    private final Faker faker = new Faker();

    private Usuario activo1;
    private Usuario activo2;
    private Usuario inactivo;
    private Ciudad ciudad;

    @BeforeEach
    void setUp() {
        //Crear la ciudad y persistirla
        ciudad = new Ciudad();
        ciudad.setNombreCiudad(faker.address().city());
        ciudad.setComuna(faker.address().city());
        em.persistAndFlush(ciudad);   // ahora es gestionada por JPA

        //Crear los usuarios usando la ciudad persistida
        activo1 = crearUsuario(true);
        activo2 = crearUsuario(true);
        inactivo = crearUsuario(false);

        em.persistAndFlush(activo1);
        em.persistAndFlush(activo2);
        em.persistAndFlush(inactivo);
    }

    private Usuario crearUsuario(boolean activo) {
        Usuario u = new Usuario();
        u.setRutUsuario(faker.regexify("\\d{7,8}-[0-9K]"));
        u.setNombreUsuario(faker.name().fullName());
        u.setFechaNacimiento(faker.date().birthday(18, 65)
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        u.setTelefonoUsuario(faker.phoneNumber().cellPhone());
        u.setEmailUsuario(faker.internet().emailAddress());
        u.setActivo(activo);
        u.setIdCiudad(ciudad);   // asigna el objeto Ciudad persistido
        return u;
    }

    @Test
    void findAllByActivoTrueDeberiaRetornarSoloActivosTest() {
        List<Usuario> resultado = repository.findAllByActivoTrue();

        assertThat(resultado)
                .hasSize(2)
                .extracting(Usuario::getIdUsuario)
                .containsExactlyInAnyOrder(activo1.getIdUsuario(), activo2.getIdUsuario());
    }

    @Test
    void findAllByActivoTrueCuandoNoHayActivosDeberiaRetornarListaVaciaTest() {
        em.remove(activo1);
        em.remove(activo2);
        em.flush();

        List<Usuario> resultado = repository.findAllByActivoTrue();

        assertThat(resultado).isEmpty();
    }

    @Test
    void findByIdUsuarioAndActivoTrueCuandoExisteYEstaActivoDeberiaRetornarUsuarioTest() {
        Optional<Usuario> resultado = repository.findByIdUsuarioAndActivoTrue(activo1.getIdUsuario());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdUsuario()).isEqualTo(activo1.getIdUsuario());
    }

    @Test
    void findByIdUsuarioAndActivoTrueCuandoExistePeroInactivoDeberiaRetornarEmptyTest() {
        Optional<Usuario> resultado = repository.findByIdUsuarioAndActivoTrue(inactivo.getIdUsuario());

        assertThat(resultado).isEmpty();
    }

    @Test
    void findByIdUsuarioAndActivoTrueCuandoIdNoExisteDeberiaRetornarEmptyTest() {
        Optional<Usuario> resultado = repository.findByIdUsuarioAndActivoTrue(999L);

        assertThat(resultado).isEmpty();
    }
}