package com.centromedico.usuarios.config;

import com.centromedico.usuarios.model.Ciudad;
import com.centromedico.usuarios.model.Usuario;
import com.centromedico.usuarios.repository.CiudadRepository;
import com.centromedico.usuarios.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CiudadRepository ciudadRepository;

    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        dataInitializer = new DataInitializer(
                usuarioRepository,
                ciudadRepository,
                new Faker(new Locale("es", "CL"), new Random(1234L))
        );
    }


    @Test
    void runCuandoNoHayDatosCrearCiudadesYUsuariosConDataFakerTest() {
        when(usuarioRepository.count()).thenReturn(0L);
        when(ciudadRepository.findAll()).thenReturn(List.of());
        when(ciudadRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(usuarioRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        dataInitializer.run();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Ciudad>> ciudadesCaptor = ArgumentCaptor.forClass((Class) List.class);
        verify(ciudadRepository).saveAll(ciudadesCaptor.capture());
        List<Ciudad> ciudadesGuardadas = ciudadesCaptor.getValue();
        assertThat(ciudadesGuardadas).hasSize(2);
        assertThat(ciudadesGuardadas)
                .allSatisfy(ciudad -> {
                    assertThat(ciudad.getNombreCiudad()).isNotBlank();
                    assertThat(ciudad.getComuna()).isNotBlank();
                });

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Usuario>> usuariosCaptor = ArgumentCaptor.forClass((Class) List.class);
        verify(usuarioRepository).saveAll(usuariosCaptor.capture());
        List<Usuario> usuariosGuardados = usuariosCaptor.getValue();
        assertThat(usuariosGuardados).hasSize(10);
        assertThat(usuariosGuardados)
                .allSatisfy(usuario -> {
                    assertThat(usuario.getRutUsuario()).matches("\\d{8}-\\d");
                    assertThat(usuario.getNombreUsuario()).isNotBlank();
                    assertThat(usuario.getFechaNacimiento()).isNotNull();
                    assertThat(usuario.getIdCiudad()).isNotNull();
                    assertThat(usuario.getTelefonoUsuario()).startsWith("+569");
                    assertThat(usuario.getEmailUsuario()).contains("@");
                    assertThat(usuario.getActivo()).isTrue();
                    assertThat(usuario.getIdCita()).isNull();
                });
    }

    @Test
    void runCuandoYaExistenUsuariosOmitirLaCargaTest() {
        when(usuarioRepository.count()).thenReturn(1L);

        dataInitializer.run();

        verify(usuarioRepository).count();
        verify(usuarioRepository, never()).saveAll(anyList());
        verifyNoInteractions(ciudadRepository);
    }
}
