package com.centromedico.administracionusuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long idUsuario;
    private String rutUsuario;
    private Long idCita;
    private String nombreUsuario;
    private LocalDate fechaNacimiento;
    private Long idCiudad;
    private String telefonoUsuario;
    private String emailUsuario;
}
