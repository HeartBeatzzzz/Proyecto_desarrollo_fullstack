package com.centromedico.usuarios.controller;

import com.centromedico.usuarios.assemblers.CitaModelAssembler;
import com.centromedico.usuarios.dto.CitaCancelacionRequestDTO;
import com.centromedico.usuarios.dto.CitaCreacionRequestDTO;
import com.centromedico.usuarios.dto.CitaResponseDTO;
import com.centromedico.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/citas")
@RequiredArgsConstructor
public class UsuarioCitaController {

    private final UsuarioService usuarioService;
    private final CitaModelAssembler citaModelAssembler;

    @GetMapping
    public ResponseEntity<CitaResponseDTO> obtenerCita(@PathVariable Long usuarioId) {
        CitaResponseDTO cita = usuarioService.obtenerCitaPorUsuario(usuarioId);
        return cita != null
                ? ResponseEntity.ok(agregarEnlacesCita(cita, usuarioId))
                : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CitaResponseDTO> agendarCita(
            @PathVariable Long usuarioId,
            @Valid @RequestBody CitaCreacionRequestDTO request) {
        CitaResponseDTO nuevaCita = usuarioService.agendarCita(usuarioId, request);
        return ResponseEntity.status(201).body(agregarEnlacesCita(nuevaCita, usuarioId));
    }

    @PutMapping("/{citaId}/cancelar")
    public ResponseEntity<CitaResponseDTO> cancelarCita(
            @PathVariable Long usuarioId,
            @PathVariable Long citaId,
            @RequestBody(required = false) CitaCancelacionRequestDTO request) {
        CitaResponseDTO citaCancelada = usuarioService.cancelarCita(usuarioId, citaId, request);
        return ResponseEntity.ok(agregarEnlacesCita(citaCancelada, usuarioId));
    }

    private CitaResponseDTO agregarEnlacesCita(CitaResponseDTO cita, Long usuarioId) {
        cita.add(linkTo(methodOn(UsuarioCitaController.class)
                .obtenerCita(usuarioId)).withSelfRel());
        cita.add(linkTo(methodOn(UsuarioController.class)
                .obtenerPorId(usuarioId)).withRel("usuario"));

        if (cita.getIdCita() != null) {
            cita.add(linkTo(methodOn(UsuarioCitaController.class)
                    .cancelarCita(usuarioId, cita.getIdCita(), null)).withRel("cancelar"));
        }

        return cita;
    }
}
