package com.centromedico.usuarios.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Como exceptionHandler no tiene lógica propia
//Se usa DummyController para tener un endpoint que falle con la validación
@WebMvcTest(controllers = DummyController.class)  // cargamos solo el controlador de prueba
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //Manejo de errores de validación (@Valid)
    @Test
    void handleValidationDatosInvalidosRetornar400ConMapaDeErroresTest() throws Exception {
        TestDto dtoInvalido = new TestDto("");   // @NotBlank falla

        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // Verifica que el JSON tenga la clave "nombre" con el mensaje por defecto
                .andExpect(jsonPath("$.nombre").value("El campo nombre no debe estar vacío"))
                // Opcional: asegura que no tenga una clave "errors" (formato de array no deseado)
                .andExpect(jsonPath("$.errors").doesNotExist());
    }

    //Manejo de RuntimeException
    @Test
    void handleRuntimeSeLanzaRuntimeExceptionRetornar400ConMensajeDeErrorTest() throws Exception {
        mockMvc.perform(get("/test/runtime"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error").value("Error simulado desde el controlador"));
    }
}


//DTO y controlador de prueba (internos al test para mayor claridad)
@Data
@NoArgsConstructor
@AllArgsConstructor
class TestDto {
    @NotBlank(message = "El campo nombre no debe estar vacío")
    private String nombre;
}

@RestController
@RequestMapping("/test")
class DummyController {

    @PostMapping("/validation")
    public ResponseEntity<String> validationEndpoint(@Valid @RequestBody TestDto dto) {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/runtime")
    public ResponseEntity<String> runtimeEndpoint() {
        throw new RuntimeException("Error simulado desde el controlador");
    }
}