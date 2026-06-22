package com.centromedico.usuarios.assemblers;

import com.centromedico.usuarios.dto.UsuarioResponseDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class UsuarioModelAssemblerTest {

    private final UsuarioModelAssembler assembler = new UsuarioModelAssembler();

    @Test
    void toModelUsuarioDeberiaIncluirSelfYCitasTest() {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(10L);

        UsuarioResponseDTO result = assembler.toModel(dto, false);

        assertThat(result.getLinks()).hasSize(2);
        assertThat(result.getLink("self")).isPresent()
                .get().extracting(link -> link.getHref())
                .isEqualTo("/api/usuarios/10");               // sin /admin
        assertThat(result.getLink("citas")).isPresent()
                .get().extracting(link -> link.getHref())
                .isEqualTo("/api/usuarios/10/citas");
    }

    @Test
    void toModelAdmiDeberiaIncluirSelfAdminYCitasTest() {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(20L);

        UsuarioResponseDTO result = assembler.toModel(dto, true);

        assertThat(result.getLink("self")).isPresent()
                .get().extracting(link -> link.getHref())
                .isEqualTo("/api/usuarios/admin/20");        // con /admin
        assertThat(result.getLink("citas")).isPresent();
    }
}