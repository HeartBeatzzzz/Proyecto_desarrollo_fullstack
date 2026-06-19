package com.centromedico.usuarios.dto;

import jakarta.validation.constraints.NotNull;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

//DTO necesario para enviar una solicitud de cancelacion al ms de reservas
public class CitaCancelacionRequestDTO {

    @NotNull(message = "El ID de la cita es obligatorio")
    private Long idCita;

    private Long idUsuario;
}
