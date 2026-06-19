package com.reservas.reservas.assemblers;

import com.reservas.reservas.controller.ReservaControllerV2;
import com.reservas.reservas.dto.ReservaResponseDTO;
import com.reservas.reservas.model.Reserva;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReservaModelAssembler implements RepresentationModelAssembler<ReservaResponseDTO, EntityModel<ReservaResponseDTO>> {
    @Override
    public EntityModel<ReservaResponseDTO> toModel(ReservaResponseDTO reserva){
        return EntityModel.of(reserva,
                linkTo(methodOn(ReservaControllerV2.class).listarReservas())
                        .withRel("Reservas"),
                linkTo(methodOn(ReservaControllerV2.class).buscarPorId(reserva.getIdCita()))
                        .withSelfRel()
        );
    }
}
