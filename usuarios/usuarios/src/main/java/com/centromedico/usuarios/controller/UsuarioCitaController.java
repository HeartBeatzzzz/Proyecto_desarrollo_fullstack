package com.centromedico.usuarios.controller;

import com.centromedico.usuarios.dto.CitaCancelacionRequestDTO;
import com.centromedico.usuarios.dto.CitaCreacionRequestDTO;
import com.centromedico.usuarios.dto.CitaResponseDTO;
import com.centromedico.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/citas")
@RequiredArgsConstructor
public class UsuarioCitaController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<CitaResponseDTO> obtenerCita(@PathVariable Long usuarioId) {
        CitaResponseDTO cita = usuarioService.obtenerCitaPorUsuario(usuarioId);
        return cita != null ? ResponseEntity.ok(cita) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CitaResponseDTO> agendarCita(
            @PathVariable Long usuarioId,
            @Valid @RequestBody CitaCreacionRequestDTO request) {
        CitaResponseDTO nuevaCita = usuarioService.agendarCita(usuarioId, request);
        return ResponseEntity.status(201).body(nuevaCita);
    }

    @PutMapping("/{citaId}/cancelar")
    public ResponseEntity<CitaResponseDTO> cancelarCita(
            @PathVariable Long usuarioId,
            @PathVariable Long citaId,
            @RequestBody(required = false) CitaCancelacionRequestDTO request) {
        CitaResponseDTO citaCancelada = usuarioService.cancelarCita(usuarioId, citaId, request);
        return ResponseEntity.ok(citaCancelada);
    }
}