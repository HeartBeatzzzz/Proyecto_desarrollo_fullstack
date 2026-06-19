package com.centromedico.usuarios.config;

import com.centromedico.usuarios.controller.UsuarioController;
import com.centromedico.usuarios.dto.UsuarioResponseDTO;
import com.centromedico.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {UsuarioController.class, com.centromedico.usuarios.controller.AuthController.class}, properties = {
        "app.security.admin.username=admin",
        "app.security.admin.password=admin123",
        "app.security.user.username=usuario",
        "app.security.user.password=usuario123"
})
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    @WithAnonymousUser
    void debeBloquearEndpointAdminSinCredenciales() throws Exception {
        mockMvc.perform(get("/api/usuarios/admin"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login/admin"));
    }

    @Test
    @WithAnonymousUser
    void debeBloquearEndpointUsuarioSinCredenciales() throws Exception {
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login/usuario"));
    }

    @Test
    void debeMostrarLoginAdmin() throws Exception {
        mockMvc.perform(get("/login/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Login de Administrador")));
    }

    @Test
    void debeMostrarLoginUsuario() throws Exception {
        mockMvc.perform(get("/login/usuario"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Login de Usuario")));
    }

    @Test
    void debePermitirLoginDeAdministrador() throws Exception {
        mockMvc.perform(formLogin("/perform_login").user("admin").password("admin123"))
                .andExpect(status().isFound());
    }

    @Test
    void debePermitirLoginDeUsuario() throws Exception {
        mockMvc.perform(formLogin("/perform_login").user("usuario").password("usuario123"))
                .andExpect(status().isFound())
                .andExpect(authenticated().withUsername("usuario"));
    }

    @Test
    void debePermitirEndpointAdminConCredencialesDeAdmin() throws Exception {
        when(usuarioService.obtenerTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/usuarios/admin").with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk());
    }

    @Test
    void debePermitirEndpointUsuarioConCredencialesDeUsuario() throws Exception {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO(
                1L,
                "12345678-9",
                null,
                "Juan Perez",
                LocalDate.of(1995, 5, 12),
                1L,
                "+56911111111",
                "juan.perez@gmail.com"
        );

        when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/1").with(httpBasic("usuario", "usuario123")))
                .andExpect(status().isOk());
    }
}
