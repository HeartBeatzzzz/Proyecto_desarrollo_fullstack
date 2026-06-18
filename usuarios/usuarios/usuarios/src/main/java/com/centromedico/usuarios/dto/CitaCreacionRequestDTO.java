package com.centromedico.usuarios.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaCreacionRequestDTO {

    private Long idUsuario;

    @NotNull(message = "El id del médico es obligatorio")
    private Long idEmp;

    @NotNull(message = "La fecha es obligatoria")
    @Future(message = "La fecha debe ser futura") // evita fechas pasadas
    private LocalDate fechaCita;

    @NotNull(message = "El tipo de cita es obligatorio")
    private String tipoCita;

    // NO incluir disponibilidad (la asigna el servicio de reservas)
}