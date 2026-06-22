package com.centromedico.usuarios.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Relation(itemRelation = "usuario", collectionRelation = "usuarios")
public class UsuarioResponseDTO extends RepresentationModel<UsuarioResponseDTO> {

    private Long idUsuario;
    private String rutUsuario;
    private Long idCita;
    private String nombreUsuario;
    private LocalDate fechaNacimiento;
    private Long idCiudad;
    private String telefonoUsuario;
    private String emailUsuario;

}
