package com.centromedico.usuarios.client;

import com.centromedico.usuarios.dto.CitaCancelacionRequestDTO;
import com.centromedico.usuarios.dto.CitaCreacionRequestDTO;
import com.centromedico.usuarios.dto.CitaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "servicio-reservas", url = "${reservas.service.url}")
public interface CitaClient {

    @PostMapping("/api/citas")
    CitaResponseDTO crearCita(@RequestBody CitaCreacionRequestDTO request);

    @PutMapping("/api/citas/{idCita}/cancelar")
    CitaResponseDTO cancelarCita(@PathVariable("idCita") Long idCita,
                                 @RequestBody(required = false) CitaCancelacionRequestDTO request);

    @GetMapping("/api/citas/{idCita}")
    CitaResponseDTO obtenerCitaPorId(@PathVariable("idCita") Long idCita);
}