package com.centromedico.usuarios.service;

import com.centromedico.usuarios.client.CitaClient;
import com.centromedico.usuarios.dto.CitaCancelacionRequestDTO;
import com.centromedico.usuarios.dto.CitaCreacionRequestDTO;
import com.centromedico.usuarios.dto.CitaResponseDTO;
import com.centromedico.usuarios.dto.UsuarioRequestDTO;
import com.centromedico.usuarios.dto.UsuarioResponseDTO;
import com.centromedico.usuarios.model.Ciudad;
import com.centromedico.usuarios.model.Usuario;
import com.centromedico.usuarios.repository.CiudadRepository;
import com.centromedico.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CiudadRepository ciudadRepository;

    @Mock
    private CitaClient citaClient;

    private UsuarioService usuarioService;
    private Ciudad ciudad;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(usuarioRepository, ciudadRepository, citaClient);
        ciudad = new Ciudad(1L, "Valparaiso", "Valparaiso");
    }

    @Test
    void obtenerTodosDevuelveUsuariosActivosMapeadosTest() {
        Usuario usuario = crearUsuarioTest(10L);
        when(usuarioRepository.findAllByActivoTrue()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().getIdUsuario()).isEqualTo(10L);
        assertThat(resultado.getFirst().getIdCiudad()).isEqualTo(1L);
        verify(usuarioRepository).findAllByActivoTrue();
    }

    @Test
    void obtenerPorIdDevuelveUsuarioActivoCuandoExisteTest() {
        Usuario usuario = crearUsuarioTest(10L);
        when(usuarioRepository.findByIdUsuarioAndActivoTrue(10L)).thenReturn(Optional.of(usuario));

        Optional<UsuarioResponseDTO> resultado = usuarioService.obtenerPorId(10L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmailUsuario()).isEqualTo("juan.perez@gmail.com");
    }

    @Test
    void crearGuardaUsuarioActivoConCiudadExistenteTest() {
        UsuarioRequestDTO request = crearUsuarioRequestTest();
        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(ciudad));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuarioGuardado = invocation.getArgument(0);
            usuarioGuardado.setIdUsuario(20L);
            return usuarioGuardado;
        });

        UsuarioResponseDTO resultado = usuarioService.crear(request);

        assertThat(resultado.getIdUsuario()).isEqualTo(20L);
        assertThat(resultado.getRutUsuario()).isEqualTo("12345678-9");

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        assertThat(usuarioCaptor.getValue().getActivo()).isTrue();
        assertThat(usuarioCaptor.getValue().getIdCiudad()).isEqualTo(ciudad);
    }

    @Test
    void crearLanzaExcepcionCuandoCiudadNoExisteTest() {
        UsuarioRequestDTO request = crearUsuarioRequestTest();
        when(ciudadRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.crear(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("La ciudad con ID 1 no existe");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizarModificaUsuarioExistenteTest() {
        Usuario usuario = crearUsuarioTest(10L);
        UsuarioRequestDTO request = crearUsuarioRequestTest();
        request.setNombreUsuario("Maria Perez");
        when(usuarioRepository.findByIdUsuarioAndActivoTrue(10L)).thenReturn(Optional.of(usuario));
        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(ciudad));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Optional<UsuarioResponseDTO> resultado = usuarioService.actualizar(10L, request);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombreUsuario()).isEqualTo("Maria Perez");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void eliminarMarcaUsuarioComoInactivoTest() {
        Usuario usuario = crearUsuarioTest(10L);
        when(usuarioRepository.findByIdUsuarioAndActivoTrue(10L)).thenReturn(Optional.of(usuario));

        boolean resultado = usuarioService.eliminar(10L);

        assertThat(resultado).isTrue();
        assertThat(usuario.getActivo()).isFalse();
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void eliminarRetornaFalseCuandoUsuarioNoExisteTest() {
        when(usuarioRepository.findByIdUsuarioAndActivoTrue(99L)).thenReturn(Optional.empty());

        boolean resultado = usuarioService.eliminar(99L);

        assertThat(resultado).isFalse();
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void agendarCitaSeteaUsuarioEnRequestYGuardaIdCitaTest() {
        Usuario usuario = crearUsuarioTest(10L);
        CitaCreacionRequestDTO request = new CitaCreacionRequestDTO(
                null,
                5L,
                LocalDate.now().plusDays(3),
                "GENERAL"
        );
        CitaResponseDTO citaCreada = new CitaResponseDTO(
                30L,
                "GENERAL",
                LocalDate.now().plusDays(3),
                "5",
                "AGENDADA"
        );
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));
        when(citaClient.crearCita(request)).thenReturn(citaCreada);

        CitaResponseDTO resultado = usuarioService.agendarCita(10L, request);

        assertThat(resultado.getIdCita()).isEqualTo(30L);
        assertThat(request.getIdUsuario()).isEqualTo(10L);
        assertThat(usuario.getIdCita()).isEqualTo(30L);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void cancelarCitaLimpiaIdCitaCuandoReservaRespondeCanceladaTest() {
        Usuario usuario = crearUsuarioTest(10L);
        usuario.setIdCita(30L);
        CitaCancelacionRequestDTO request = new CitaCancelacionRequestDTO();
        CitaResponseDTO citaCancelada = new CitaResponseDTO(
                30L,
                "GENERAL",
                LocalDate.now().plusDays(3),
                "5",
                "CANCELADA"
        );
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));
        when(citaClient.cancelarCita(30L, request)).thenReturn(citaCancelada);

        CitaResponseDTO resultado = usuarioService.cancelarCita(10L, 30L, request);

        assertThat(resultado.getDisponibilidadCita()).isEqualTo("CANCELADA");
        assertThat(request.getIdUsuario()).isEqualTo(10L);
        assertThat(request.getIdCita()).isEqualTo(30L);
        assertThat(usuario.getIdCita()).isNull();
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void cancelarCitaLanzaExcepcionSiCitaNoPerteneceAlUsuarioTest() {
        Usuario usuario = crearUsuarioTest(10L);
        usuario.setIdCita(40L);
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> usuarioService.cancelarCita(10L, 30L, new CitaCancelacionRequestDTO()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("La cita con ID 30 no pertenece al usuario 10");

        verify(citaClient, never()).cancelarCita(any(), any());
        verify(usuarioRepository, never()).save(any());
    }

    private Usuario crearUsuarioTest(Long idUsuario) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setRutUsuario("12345678-9");
        usuario.setNombreUsuario("Juan Perez");
        usuario.setFechaNacimiento(LocalDate.of(1995, 5, 12));
        usuario.setIdCiudad(ciudad);
        usuario.setTelefonoUsuario("+56911111111");
        usuario.setEmailUsuario("juan.perez@gmail.com");
        usuario.setActivo(true);
        return usuario;
    }

    private UsuarioRequestDTO crearUsuarioRequestTest() {
        return new UsuarioRequestDTO(
                "12345678-9",
                "Juan Perez",
                LocalDate.of(1995, 5, 12),
                1L,
                "+56911111111",
                "juan.perez@gmail.com"
        );
    }
}
