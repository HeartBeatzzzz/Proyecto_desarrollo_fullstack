package com.centromedico.usuarios.assemblers;

import com.centromedico.usuarios.dto.CitaResponseDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CitaModelAssemblerTest {

    private final CitaModelAssembler assembler = new CitaModelAssembler();

    @Test
    void toModelCitaExistenteDeberiaIncluirSelfUsuarioYCancelarTest() {
        CitaResponseDTO cita = new CitaResponseDTO();
        cita.setIdCita(100L);

        CitaResponseDTO result = assembler.toModel(cita, 5L); // usuarioId = 5

        assertThat(result.getLinks()).hasSize(3);
        assertThat(result.getLink("self")).isPresent()
                .get().extracting(link -> link.getHref())
                .isEqualTo("/api/usuarios/5/citas");
        assertThat(result.getLink("usuario")).isPresent()
                .get().extracting(link -> link.getHref())
                .isEqualTo("/api/usuarios/5");
        assertThat(result.getLink("cancelar")).isPresent()
                .get().extracting(link -> link.getHref())
                .isEqualTo("/api/usuarios/5/citas/100/cancelar");
    }

    @Test
    void toModelCuandoIdCitaEsNuloNDebeIncluirCancelarTest() {
        CitaResponseDTO cita = new CitaResponseDTO(); // idCita = null

        CitaResponseDTO result = assembler.toModel(cita, 5L);

        assertThat(result.getLink("cancelar")).isNotPresent();
        assertThat(result.getLinks()).hasSize(2); // solo self y usuario
    }
}