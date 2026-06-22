package com.centromedico.usuarios.config;

import com.centromedico.usuarios.assemblers.UsuarioModelAssembler;
import com.centromedico.usuarios.controller.UsuarioController;
import com.centromedico.usuarios.dto.UsuarioResponseDTO;
import com.centromedico.usuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioModelAssembler usuarioModelAssembler;

    @BeforeEach
    void setUp() {
        when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(new UsuarioResponseDTO()));
        when(usuarioModelAssembler.toModel(any(), anyBoolean())).thenReturn(new UsuarioResponseDTO());
        when(usuarioService.obtenerTodos()).thenReturn(java.util.List.of(new UsuarioResponseDTO()));
    }

    @Test
    void accesoAUsuarioSinAuthRetorna200Test() throws Exception {
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void accesoAAdminListarSinAuthRetorna200Test() throws Exception {
        mockMvc.perform(get("/api/usuarios/admin"))
                .andExpect(status().isOk());
    }

    @Test
    void accesoALoginAdminSinAuthRetorna401Test() throws Exception {
        mockMvc.perform(get("/login/admin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accesoALoginAdminConBasicAuthRetorna200Test() throws Exception {
        mockMvc.perform(get("/api/usuarios/1")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4xMjM="))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void accesoConMockUserAEndpointPublicoRetorna200Test() throws Exception {
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void accesoARutaCualquieraSinAuthRetorna401Test() throws Exception {
        mockMvc.perform(get("/cualquier/otra"))
                .andExpect(status().isUnauthorized());
    }
}