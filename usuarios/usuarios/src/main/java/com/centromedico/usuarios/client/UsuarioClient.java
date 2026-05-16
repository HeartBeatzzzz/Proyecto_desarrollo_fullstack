package com.centromedico.usuarios.client;

import com.centromedico.usuarios.dto.UsuarioResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "servicio-usuarios", url = "${usuarios-service.url}")
public interface UsuarioClient {

    @GetMapping("/api/v1/usuarios")
    List<UsuarioResponseDTO> obtenerTodos();

    @GetMapping("/api/v1/usuarios/{id}")
    UsuarioResponseDTO obtenerPorId(@PathVariable("id") Long id);
}