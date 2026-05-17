package com.centromedico.administracionusuarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El RUT es obligatorio")
    private String rutUsuario;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombreUsuario;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    @NotNull(message = "La ciudad es obligatoria")
    private Long idCiudad;

    @NotBlank(message = "El telefono es obligatorio")
    private String telefonoUsuario;

    @NotBlank(message = "El email es obligatorio")
    private String emailUsuario;
}
