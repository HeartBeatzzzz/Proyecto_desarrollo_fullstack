package com.reservas.reservas.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequestDTO {

    private Long idUsuario;

    @NotNull(message = "El id del medico es obligatorio")
    private Long idEmp;


    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha no puede ser pasada")
    private LocalDate fechaCita;

    @NotBlank(message = "El tipo de cita es obligatorio")
    private String tipoCita;

}
