package com.centromedico.usuarios.assemblers;

import com.centromedico.usuarios.controller.UsuarioCitaController;
import com.centromedico.usuarios.controller.UsuarioController;
import com.centromedico.usuarios.dto.CitaResponseDTO;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CitaModelAssembler {

    public CitaResponseDTO toModel(CitaResponseDTO cita, Long usuarioId) {
        //enlace self
        cita.add(linkTo(methodOn(UsuarioCitaController.class)
                .obtenerCita(usuarioId)).withSelfRel());

        //enlace al usuario dueño de la cita
        cita.add(linkTo(methodOn(UsuarioController.class)
                .obtenerPorId(usuarioId)).withRel("usuario"));

        //enlace para cancelar
        if (cita.getIdCita() != null) {
            cita.add(linkTo(methodOn(UsuarioCitaController.class)
                    .cancelarCita(usuarioId, cita.getIdCita(), null)).withRel("cancelar"));
        }

        return cita;
    }
}