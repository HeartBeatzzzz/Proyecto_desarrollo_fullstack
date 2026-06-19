package com.centromedico.usuarios.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

/*Al recibir el resultado de la solicitud de los REQUEST, el RESPONSE
 * guarda los datos obtenidos o modificados (conocer estado final de cita, setear idCita en usuario)*/
public class CitaResponseDTO {
    private Long idCita;
    private String tipoCita;
    private LocalDate fechaCita;
    private String idEmp;
    private String disponibilidadCita;

}
