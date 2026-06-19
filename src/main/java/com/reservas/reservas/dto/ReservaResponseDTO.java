package com.reservas.reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaResponseDTO {

    private Long idCita;

    private String tipoCita;

    private LocalDate fechaCita;

    private String idEmp;

    private  String disponibilidadCita;
}
