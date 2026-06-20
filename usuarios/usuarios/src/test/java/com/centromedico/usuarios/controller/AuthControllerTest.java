package com.centromedico.usuarios.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginUsuarioRetornarHtmlConFormularioTest() throws Exception {
        mockMvc.perform(get("/login/usuario"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("<title>Login Usuario</title>")))
                .andExpect(content().string(containsString("<h1>Login de Usuario</h1>")))
                .andExpect(content().string(containsString("action=\"/perform_login\"")))
                .andExpect(content().string(containsString("name=\"username\"")))
                .andExpect(content().string(containsString("name=\"password\"")));
    }

    @Test
    void loginAdminRetornarHtmlConFormularioTest() throws Exception {
        mockMvc.perform(get("/login/admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("<title>Login Admin Usuarios</title>")))
                .andExpect(content().string(containsString("<h1>Login de Administrador</h1>")))
                .andExpect(content().string(containsString("action=\"/perform_login\"")))
                .andExpect(content().string(containsString("name=\"username\"")))
                .andExpect(content().string(containsString("name=\"password\"")));
    }
}
