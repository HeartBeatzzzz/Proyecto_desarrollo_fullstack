package com.centromedico.usuarios.controller;

import com.centromedico.usuarios.dto.CitaCancelacionRequestDTO;
import com.centromedico.usuarios.dto.CitaCreacionRequestDTO;
import com.centromedico.usuarios.dto.CitaResponseDTO;
import com.centromedico.usuarios.exception.GlobalExceptionHandler;
import com.centromedico.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import net.datafaker.providers.healthcare.HealthcareFaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureWebMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioCitaController.class)
@AutoConfigureWebMvc
@Import(GlobalExceptionHandler.class)
class UsuarioCitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private final HealthcareFaker faker = new HealthcareFaker();

    private CitaResponseDTO citaResponse;
    private CitaCreacionRequestDTO citaCreacionRequest;
    private CitaCancelacionRequestDTO citaCancelacionRequest;

    @BeforeEach
    void setUp() {
        //generar datos realistas con DataFaker
        Long idCita = faker.number().numberBetween(1L, 1000L);
        String tipoCita = faker.medicalProcedure().icd10(); // valor ficticio
        LocalDate fechaCita = LocalDate.now().plusDays(faker.number().numberBetween(1, 30));
        String idEmp = String.valueOf(faker.number().numberBetween(100L, 999L));
        String disponibilidad = faker.options().option("Disponible", "Reservada", "Cancelada");

        citaResponse = new CitaResponseDTO(
                idCita,
                tipoCita,
                fechaCita,
                idEmp,
                disponibilidad
        );

        citaCreacionRequest = new CitaCreacionRequestDTO(
                null,                           // idUsuario no se envía desde el controlador
                faker.number().numberBetween(100L, 999L),  // idEmp
                fechaCita,
                tipoCita
        );

        citaCancelacionRequest = new CitaCancelacionRequestDTO(
                idCita,                         //mismo id de la cita simulada
                faker.number().numberBetween(1L, 1000L)   // idUsuario random
        );
    }

    //GET /api/usuarios/{usuarioId}/citas

    @Test
    void obtenerCitaCuandoExisteRetornar200YCitaTest() throws Exception {
        when(usuarioService.obtenerCitaPorUsuario(1L)).thenReturn(citaResponse);

        mockMvc.perform(get("/api/usuarios/1/citas"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idCita").value(citaResponse.getIdCita()))
                .andExpect(jsonPath("$.tipoCita").value(citaResponse.getTipoCita()))
                .andExpect(jsonPath("$.fechaCita").value(citaResponse.getFechaCita().toString()))
                .andExpect(jsonPath("$.idEmp").value(citaResponse.getIdEmp()))
                .andExpect(jsonPath("$.disponibilidadCita").value(citaResponse.getDisponibilidadCita()));

        verify(usuarioService).obtenerCitaPorUsuario(1L);
    }

    @Test
    void obtenerCitaCuandoNoExisteRetornar204NoContentTest() throws Exception {
        when(usuarioService.obtenerCitaPorUsuario(99L)).thenReturn(null);

        mockMvc.perform(get("/api/usuarios/99/citas"))
                .andExpect(status().isNoContent());

        verify(usuarioService).obtenerCitaPorUsuario(99L);
    }

    // ---------- POST /api/usuarios/{usuarioId}/citas ----------

    @Test
    void agendarCitaConDatosValidosRetornar201YCitaCreadaTest() throws Exception {
        when(usuarioService.agendarCita(eq(1L), any(CitaCreacionRequestDTO.class)))
                .thenReturn(citaResponse);

        mockMvc.perform(post("/api/usuarios/1/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citaCreacionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCita").value(citaResponse.getIdCita()))
                .andExpect(jsonPath("$.tipoCita").value(citaResponse.getTipoCita()))
                .andExpect(jsonPath("$.fechaCita").value(citaResponse.getFechaCita().toString()));

        verify(usuarioService).agendarCita(eq(1L), any(CitaCreacionRequestDTO.class));
    }

    @Test
    void agendarCitaDatosInvalidosRetornar400ConMensajesTest() throws Exception {
        CitaCreacionRequestDTO dtoInvalido = new CitaCreacionRequestDTO(
                null,
                null,
                LocalDate.now().minusDays(1),
                null
        );

        mockMvc.perform(post("/api/usuarios/1/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.idEmp").value("El id del médico es obligatorio"))
                .andExpect(jsonPath("$.fechaCita").value("La fecha debe ser futura"))
                .andExpect(jsonPath("$.tipoCita").value("El tipo de cita es obligatorio"));
    }

    //PUT /api/usuarios/{usuarioId}/citas/{citaId}/cancelar

    @Test
    void cancelarCitaConCuerpoOpcionalDeberiaRetornar200YCitaCanceladaTest() throws Exception {
        when(usuarioService.cancelarCita(eq(1L), eq(10L), any(CitaCancelacionRequestDTO.class)))
                .thenReturn(citaResponse);

        mockMvc.perform(put("/api/usuarios/1/citas/10/cancelar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citaCancelacionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCita").value(citaResponse.getIdCita()))
                .andExpect(jsonPath("$.disponibilidadCita").value(citaResponse.getDisponibilidadCita()));

        verify(usuarioService).cancelarCita(eq(1L), eq(10L), any(CitaCancelacionRequestDTO.class));
    }

    @Test
    void cancelarCitaSinCuerpoDeberiaFuncionarRetornar200Test() throws Exception {
        when(usuarioService.cancelarCita(eq(1L), eq(10L), isNull()))
                .thenReturn(citaResponse);

        mockMvc.perform(put("/api/usuarios/1/citas/10/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCita").value(citaResponse.getIdCita()));

        verify(usuarioService).cancelarCita(eq(1L), eq(10L), isNull());
    }
}
