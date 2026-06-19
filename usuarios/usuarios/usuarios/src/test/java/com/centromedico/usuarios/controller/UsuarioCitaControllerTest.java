package com.centromedico.usuarios.controller;

import com.centromedico.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UsuarioCitaControllerTest.class)
public class UsuarioCitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioCitaController usuarioCitaController;

    @BeforeEach
    void setUp(){
        usuarioService = new UsuarioService()
        usuarioService.set;
    }
}
