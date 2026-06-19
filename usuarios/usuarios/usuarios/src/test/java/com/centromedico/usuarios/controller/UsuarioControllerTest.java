package com.centromedico.usuarios.controller;

import com.centromedico.usuarios.dto.UsuarioRequestDTO;
import com.centromedico.usuarios.dto.UsuarioResponseDTO;
import com.centromedico.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioResponseDTO usuarioResponseDTO;
    private UsuarioRequestDTO usuarioRequestDTO;

    @BeforeEach
    void setUp() {
        usuarioResponseDTO = new UsuarioResponseDTO(
                1L,
                "12345678-9",
                null,
                "Juan Perez",
                LocalDate.of(1990, 1, 1),
                5L,
                "+56912345678",
                "juan@example.com"
        );

        usuarioRequestDTO = new UsuarioRequestDTO(
                "12345678-9",
                "Juan Perez",
                LocalDate.of(1990, 1, 1),
                5L,
                "+56912345678",
                "juan@example.com"
        );
    }

    @Test
    void listarTodosParaAdmin_deberiaRetornarListaDeUsuarios() throws Exception {
        when(usuarioService.obtenerTodos()).thenReturn(List.of(usuarioResponseDTO));

        mockMvc.perform(get("/api/usuarios/admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].idUsuario").value(1L))
                .andExpect(jsonPath("$[0].nombreUsuario").value("Juan Perez"));

        verify(usuarioService).obtenerTodos();
    }

    @Test
    void obtenerPorIdAdmin_cuandoExiste_deberiaRetornarUsuario() throws Exception {
        when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuarioResponseDTO));

        mockMvc.perform(get("/api/usuarios/admin/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.emailUsuario").value("juan@example.com"));

        verify(usuarioService).obtenerPorId(1L);
    }

    @Test
    void obtenerPorIdAdmin_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(usuarioService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/admin/99"))
                .andExpect(status().isNotFound());

        verify(usuarioService).obtenerPorId(99L);
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarUsuario() throws Exception {
        when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuarioResponseDTO));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1L));

        verify(usuarioService).obtenerPorId(1L);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(usuarioService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());

        verify(usuarioService).obtenerPorId(99L);
    }

    @Test
    void crearAdmin_conDTOValido_deberiaRetornar201() throws Exception {
        when(usuarioService.crear(any(UsuarioRequestDTO.class))).thenReturn(usuarioResponseDTO);

        mockMvc.perform(post("/api/usuarios/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.nombreUsuario").value("Juan Perez"));

        verify(usuarioService).crear(any(UsuarioRequestDTO.class));
    }

    @Test
    void crearAdmin_conDTOInvalido_deberiaRetornar400() throws Exception {
        UsuarioRequestDTO dtoInvalido = new UsuarioRequestDTO(
                "",
                "",
                null,
                null,
                "",
                ""
        );

        mockMvc.perform(post("/api/usuarios/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearUsuario_conDTOValido_deberiaRetornar201() throws Exception {
        when(usuarioService.crear(any(UsuarioRequestDTO.class))).thenReturn(usuarioResponseDTO);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1L));

        verify(usuarioService).crear(any(UsuarioRequestDTO.class));
    }

    @Test
    void actualizar_cuandoExiste_deberiaRetornarUsuarioActualizado() throws Exception {
        when(usuarioService.actualizar(eq(1L), any(UsuarioRequestDTO.class)))
                .thenReturn(Optional.of(usuarioResponseDTO));

        mockMvc.perform(put("/api/usuarios/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1L));

        verify(usuarioService).actualizar(eq(1L), any(UsuarioRequestDTO.class));
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(usuarioService.actualizar(eq(99L), any(UsuarioRequestDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/usuarios/admin/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isNotFound());

        verify(usuarioService).actualizar(eq(99L), any(UsuarioRequestDTO.class));
    }

    @Test
    void eliminar_cuandoExiste_deberiaRetornar204() throws Exception {
        when(usuarioService.eliminar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/usuarios/admin/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService).eliminar(1L);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(usuarioService.eliminar(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/usuarios/admin/99"))
                .andExpect(status().isNotFound());

        verify(usuarioService).eliminar(99L);
    }
}
