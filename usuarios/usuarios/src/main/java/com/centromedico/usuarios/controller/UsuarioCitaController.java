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
@RequestMapping("api/v1/usuarios/{id}/citas")
@RequiredArgsConstructor
public class UsuarioCitaController {

    private final UsuarioService usuarioService;

    // Obtener la cita actual del usuario (si existe)
    @GetMapping
    public ResponseEntity<CitaResponseDTO> obtenerCita(@PathVariable Long id) {
        CitaResponseDTO cita = usuarioService.obtenerCitaPorUsuario(id);
        if (cita == null) {
            return ResponseEntity.noContent().build(); // 204 si no tiene cita
        }
        return ResponseEntity.ok(cita);
    }

    @PostMapping
    public ResponseEntity<CitaResponseDTO> agendarCita(
            @PathVariable Long id,
            @Valid @RequestBody CitaCreacionRequestDTO request) {
        CitaResponseDTO nuevaCita = usuarioService.agendarCita(id, request);
        return ResponseEntity.status(201).body(nuevaCita);
    }

    @PutMapping("/{idCita}/cancelar")
    public ResponseEntity<CitaResponseDTO> cancelarCita(
            @PathVariable Long id,
            @PathVariable Long idCita,
            @RequestBody(required = false) CitaCancelacionRequestDTO request) {
        CitaResponseDTO citaCancelada = usuarioService.cancelarCita(id, idCita, request);
        return ResponseEntity.ok(citaCancelada);
    }
}