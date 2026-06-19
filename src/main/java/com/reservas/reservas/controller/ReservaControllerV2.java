package com.reservas.reservas.controller;

import com.reservas.reservas.assemblers.ReservaModelAssembler;
import com.reservas.reservas.dto.ReservaResponseDTO;
import com.reservas.reservas.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/citas")
@RequiredArgsConstructor
public class ReservaControllerV2 {

    private final ReservaService reservaService;
    private final ReservaModelAssembler assembler;
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ReservaResponseDTO>>> listarReservas(){
        List<EntityModel<ReservaResponseDTO>> reservas = reservaService.listarReservas()
                .stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(
                CollectionModel.of(reservas,
                        linkTo(methodOn(ReservaControllerV2.class).listarReservas()).withSelfRel())
        );
    }
    @GetMapping("{idCita}")
    public ResponseEntity<EntityModel<ReservaResponseDTO>> buscarPorId(@PathVariable Long idCita){
        Optional<ReservaResponseDTO> reserva = reservaService.buscarPorId(idCita);
        if(reserva.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(reserva.get()));
    }
    @GetMapping("/disponibilidad/{disponibilidadCita}")
    public ResponseEntity<CollectionModel<EntityModel<ReservaResponseDTO>>> buscarPorEstadoReserva(@PathVariable String disponibilidadCita){
        List<EntityModel<ReservaResponseDTO>> reservas = reservaService.buscarPorDisponibilidad(disponibilidadCita)
                .stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(
                CollectionModel.of(reservas,
                        linkTo(methodOn(ReservaControllerV2.class).buscarPorEstadoReserva(disponibilidadCita)).withSelfRel())
        );
    }
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<CollectionModel<EntityModel<ReservaResponseDTO>>> obtenerPorUsuario(@PathVariable Long idUsuario){
        List<EntityModel<ReservaResponseDTO>> reservas = reservaService.buscarPorUsuario(idUsuario)
                .stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(
                CollectionModel.of(reservas,
                        linkTo(methodOn(ReservaControllerV2.class).obtenerPorUsuario(idUsuario)).withSelfRel())
        );
    }

    @GetMapping("/empleado/{idEmp}")
    public ResponseEntity<CollectionModel<EntityModel<ReservaResponseDTO>>> obtenerPorEmpleado(@PathVariable Long idEmp){
        List<EntityModel<ReservaResponseDTO>> reservas = reservaService.buscarPorEmpleado(idEmp)
                .stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(
                CollectionModel.of(reservas,
                        linkTo(methodOn(ReservaControllerV2.class).obtenerPorEmpleado(idEmp)).withSelfRel())
        );
    }
}
