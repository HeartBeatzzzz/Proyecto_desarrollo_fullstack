package com.reservas.reservas.controller;

import com.reservas.reservas.dto.ReservaRequestDTO;
import com.reservas.reservas.dto.ReservaResponseDTO;
import com.reservas.reservas.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Operaciones relacionadas con la gestion de reservas")
public class ReservaController {
    private final ReservaService reservaService;

    @GetMapping
    @Operation(summary = "Listar reservas", description = "Obtiene todas las reservas registradas")
    public List<ReservaResponseDTO> listarReservas(){
        return reservaService.listarReservas();
    }

    @GetMapping("/{idCita}")
    @Operation(summary = "Obtener por id", description = "Obtiene la reserva por su id")
    public ResponseEntity<ReservaResponseDTO> obtenerPorId(@PathVariable Long idCita){
        return reservaService.buscarPorId(idCita)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Obtener por id de usuario", description = "Obtiene las reservas asociadas a un usuario")
    public List<ReservaResponseDTO> obtenerPorUsuario(@PathVariable Long idUsuario){
        return reservaService.buscarPorUsuario(idUsuario);

    }
    @GetMapping("/empleado/{idEmp}")
    @Operation(summary = "Obtener por id de empleado", description = "Obtiene las reservas asociadas a un empleado")
    public List<ReservaResponseDTO> obtenerPorRutEmpleado(@PathVariable Long idEmp){
        return reservaService.buscarPorEmpleado(idEmp);

    }
    @GetMapping("/disponibilidad/{disponibilidadCita}")
    @Operation(summary = "Obtener reservas por disponibilidad", description = "Obtiene las reservas consultando por su estado de reserva")
    public List<ReservaResponseDTO> obtenerPorEstadoReserva(@PathVariable String disponibilidadCita){

        return reservaService.buscarPorDisponibilidad(disponibilidadCita);
    }

    @PostMapping
    @Operation(summary = "Crear reserva", description = "Permite crear una nueva reserva")
    public ResponseEntity<ReservaResponseDTO> crearReserva(@Valid @RequestBody ReservaRequestDTO dto){
        return ResponseEntity.status(201).body(reservaService.guardar(dto));
    }

    @PutMapping("/{idCita}/cancelar")
    @Operation(summary = "Cancelar reserva", description = "Permite cancelar una reserva por su id")
    public ResponseEntity<ReservaResponseDTO> cancelarPorId(@PathVariable Long idCita){
        return reservaService.cancelarPorId(idCita)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
