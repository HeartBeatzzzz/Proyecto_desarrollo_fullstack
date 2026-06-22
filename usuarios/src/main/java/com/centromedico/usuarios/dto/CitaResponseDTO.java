package com.centromedico.usuarios.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

/*Al recibir el resultado de la solicitud de los REQUEST, el RESPONSE
 * guarda los datos obtenidos o modificados (conocer estado final de cita, setear idCita en usuario)*/
@Relation(itemRelation = "cita", collectionRelation = "citas")
public class CitaResponseDTO extends RepresentationModel<CitaResponseDTO> {
    private Long idCita;
    private String tipoCita;
    private LocalDate fechaCita;
    private String idEmp;
    private String disponibilidadCita;

}
