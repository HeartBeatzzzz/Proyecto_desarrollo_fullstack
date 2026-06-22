package com.centromedico.usuarios.controller;

import com.centromedico.usuarios.assemblers.UsuarioModelAssembler;
import com.centromedico.usuarios.dto.UsuarioRequestDTO;
import com.centromedico.usuarios.dto.UsuarioResponseDTO;
import com.centromedico.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioModelAssembler usuarioModelAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    private UsuarioResponseDTO usuarioResponseDTO;
    private UsuarioRequestDTO usuarioRequestDTO;

    @BeforeEach
    void setUp() {
        Long id = faker.number().numberBetween(1L, 1000L);
        String nombre = faker.name().fullName();
        LocalDate fechaNac = faker.date().birthday(18, 65)
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Long idCiudad = faker.number().numberBetween(1L, 10L);
        String telefono = faker.phoneNumber().cellPhone();
        String email = faker.internet().emailAddress();

        usuarioResponseDTO = new UsuarioResponseDTO(
                id, "12345678-9", null, nombre, fechaNac, idCiudad, telefono, email
        );

        usuarioRequestDTO = new UsuarioRequestDTO(
                "12345678-9", nombre, fechaNac, idCiudad, telefono, email
        );
    }

    private UsuarioResponseDTO crearDtoConEnlaces(boolean admin) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO(
                usuarioResponseDTO.getIdUsuario(),
                usuarioResponseDTO.getRutUsuario(),
                usuarioResponseDTO.getIdCita(),
                usuarioResponseDTO.getNombreUsuario(),
                usuarioResponseDTO.getFechaNacimiento(),
                usuarioResponseDTO.getIdCiudad(),
                usuarioResponseDTO.getTelefonoUsuario(),
                usuarioResponseDTO.getEmailUsuario()
        );
        dto.add(Link.of("http://localhost/api/usuarios/" + (admin ? "admin/" : "") + dto.getIdUsuario(), "self"));
        dto.add(Link.of("http://localhost/api/usuarios/" + dto.getIdUsuario() + "/citas", "citas"));
        return dto;
    }

    @Test
    void listarTodosParaAdminRetornarListaDeUsuariosTest() throws Exception {
        UsuarioResponseDTO dtoConEnlaces = crearDtoConEnlaces(true);
        when(usuarioService.obtenerTodos()).thenReturn(List.of(usuarioResponseDTO));
        when(usuarioModelAssembler.toModel(any(UsuarioResponseDTO.class), eq(true))).thenReturn(dtoConEnlaces);

        mockMvc.perform(get("/api/usuarios/admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.usuarios.size()").value(1))
                .andExpect(jsonPath("$._embedded.usuarios[0].idUsuario").value(usuarioResponseDTO.getIdUsuario()))
                .andExpect(jsonPath("$._embedded.usuarios[0].nombreUsuario").value(usuarioResponseDTO.getNombreUsuario()))
                .andExpect(jsonPath("$._embedded.usuarios[0]._links.self.href").value("http://localhost/api/usuarios/admin/" + usuarioResponseDTO.getIdUsuario()))
                .andExpect(jsonPath("$._embedded.usuarios[0]._links.citas.href").value("http://localhost/api/usuarios/" + usuarioResponseDTO.getIdUsuario() + "/citas"));

        verify(usuarioService).obtenerTodos();
        verify(usuarioModelAssembler).toModel(any(UsuarioResponseDTO.class), eq(true));
    }

    @Test
    void obtenerPorIdAdminCuandoExisteRetornarUsuarioTest() throws Exception {
        UsuarioResponseDTO dtoConEnlaces = crearDtoConEnlaces(true);
        when(usuarioService.obtenerPorId(usuarioResponseDTO.getIdUsuario())).thenReturn(Optional.of(usuarioResponseDTO));
        when(usuarioModelAssembler.toModel(any(UsuarioResponseDTO.class), eq(true))).thenReturn(dtoConEnlaces);

        mockMvc.perform(get("/api/usuarios/admin/" + usuarioResponseDTO.getIdUsuario()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.idUsuario").value(usuarioResponseDTO.getIdUsuario()))
                .andExpect(jsonPath("$.emailUsuario").value(usuarioResponseDTO.getEmailUsuario()))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/usuarios/admin/" + usuarioResponseDTO.getIdUsuario()))
                .andExpect(jsonPath("$._links.citas.href").value("http://localhost/api/usuarios/" + usuarioResponseDTO.getIdUsuario() + "/citas"));

        verify(usuarioService).obtenerPorId(usuarioResponseDTO.getIdUsuario());
        verify(usuarioModelAssembler).toModel(any(UsuarioResponseDTO.class), eq(true));
    }

    @Test
    void obtenerPorIdAdminCuandoNoExisteRetornar404Test() throws Exception {
        when(usuarioService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/admin/99"))
                .andExpect(status().isNotFound());

        verify(usuarioService).obtenerPorId(99L);
    }

    @Test
    void obtenerPorIdCuandoExisteRetornarUsuarioTest() throws Exception {
        UsuarioResponseDTO dtoConEnlaces = crearDtoConEnlaces(false);
        when(usuarioService.obtenerPorId(usuarioResponseDTO.getIdUsuario())).thenReturn(Optional.of(usuarioResponseDTO));
        when(usuarioModelAssembler.toModel(any(UsuarioResponseDTO.class), eq(false))).thenReturn(dtoConEnlaces);

        mockMvc.perform(get("/api/usuarios/" + usuarioResponseDTO.getIdUsuario()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.idUsuario").value(usuarioResponseDTO.getIdUsuario()))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/usuarios/" + usuarioResponseDTO.getIdUsuario()))
                .andExpect(jsonPath("$._links.citas.href").value("http://localhost/api/usuarios/" + usuarioResponseDTO.getIdUsuario() + "/citas"));

        verify(usuarioService).obtenerPorId(usuarioResponseDTO.getIdUsuario());
        verify(usuarioModelAssembler).toModel(any(UsuarioResponseDTO.class), eq(false));
    }

    @Test
    void obtenerPorIdCuandoNoExisteRetornar404Test() throws Exception {
        when(usuarioService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());

        verify(usuarioService).obtenerPorId(99L);
    }

    @Test
    void crearAdminDTOValidoRetornar201Test() throws Exception {
        UsuarioResponseDTO dtoConEnlaces = crearDtoConEnlaces(true);
        when(usuarioService.crear(any(UsuarioRequestDTO.class))).thenReturn(usuarioResponseDTO);
        when(usuarioModelAssembler.toModel(any(UsuarioResponseDTO.class), eq(true))).thenReturn(dtoConEnlaces);

        mockMvc.perform(post("/api/usuarios/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.idUsuario").value(usuarioResponseDTO.getIdUsuario()))
                .andExpect(jsonPath("$.nombreUsuario").value(usuarioResponseDTO.getNombreUsuario()))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/usuarios/admin/" + usuarioResponseDTO.getIdUsuario()))
                .andExpect(jsonPath("$._links.citas.href").value("http://localhost/api/usuarios/" + usuarioResponseDTO.getIdUsuario() + "/citas"));

        verify(usuarioService).crear(any(UsuarioRequestDTO.class));
        verify(usuarioModelAssembler).toModel(any(UsuarioResponseDTO.class), eq(true));
    }

    @Test
    void crearAdminDTOInvalidoRetornar400Test() throws Exception {
        UsuarioRequestDTO dtoInvalido = new UsuarioRequestDTO("", "", null, null, "", "");

        mockMvc.perform(post("/api/usuarios/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearUsuarioDTOValidoRetornar201Test() throws Exception {
        UsuarioResponseDTO dtoConEnlaces = crearDtoConEnlaces(false);
        when(usuarioService.crear(any(UsuarioRequestDTO.class))).thenReturn(usuarioResponseDTO);
        when(usuarioModelAssembler.toModel(any(UsuarioResponseDTO.class), eq(false))).thenReturn(dtoConEnlaces);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.idUsuario").value(usuarioResponseDTO.getIdUsuario()))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/usuarios/" + usuarioResponseDTO.getIdUsuario()))
                .andExpect(jsonPath("$._links.citas.href").value("http://localhost/api/usuarios/" + usuarioResponseDTO.getIdUsuario() + "/citas"));

        verify(usuarioService).crear(any(UsuarioRequestDTO.class));
        verify(usuarioModelAssembler).toModel(any(UsuarioResponseDTO.class), eq(false));
    }

    @Test
    void actualizarCuandoExisteRetornarUsuarioActualizadoTest() throws Exception {
        UsuarioResponseDTO dtoConEnlaces = crearDtoConEnlaces(true);
        when(usuarioService.actualizar(eq(usuarioResponseDTO.getIdUsuario()), any(UsuarioRequestDTO.class)))
                .thenReturn(Optional.of(usuarioResponseDTO));
        when(usuarioModelAssembler.toModel(any(UsuarioResponseDTO.class), eq(true))).thenReturn(dtoConEnlaces);

        mockMvc.perform(put("/api/usuarios/admin/" + usuarioResponseDTO.getIdUsuario())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.idUsuario").value(usuarioResponseDTO.getIdUsuario()))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/usuarios/admin/" + usuarioResponseDTO.getIdUsuario()))
                .andExpect(jsonPath("$._links.citas.href").value("http://localhost/api/usuarios/" + usuarioResponseDTO.getIdUsuario() + "/citas"));

        verify(usuarioService).actualizar(eq(usuarioResponseDTO.getIdUsuario()), any(UsuarioRequestDTO.class));
        verify(usuarioModelAssembler).toModel(any(UsuarioResponseDTO.class), eq(true));
    }

    @Test
    void actualizarCuandoNoExisteRetornar404Test() throws Exception {
        when(usuarioService.actualizar(eq(99L), any(UsuarioRequestDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/usuarios/admin/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isNotFound());

        verify(usuarioService).actualizar(eq(99L), any(UsuarioRequestDTO.class));
    }

    @Test
    void eliminarCuandoExisteRetornar204Test() throws Exception {
        when(usuarioService.eliminar(usuarioResponseDTO.getIdUsuario())).thenReturn(true);

        mockMvc.perform(delete("/api/usuarios/admin/" + usuarioResponseDTO.getIdUsuario()))
                .andExpect(status().isNoContent());

        verify(usuarioService).eliminar(usuarioResponseDTO.getIdUsuario());
    }

    @Test
    void eliminarCuandoNoExisteRetornar404Test() throws Exception {
        when(usuarioService.eliminar(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/usuarios/admin/99"))
                .andExpect(status().isNotFound());

        verify(usuarioService).eliminar(99L);
    }
}