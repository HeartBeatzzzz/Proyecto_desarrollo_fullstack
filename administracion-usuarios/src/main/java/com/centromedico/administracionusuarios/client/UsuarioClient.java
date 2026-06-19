package com.centromedico.administracionusuarios.client;

import com.centromedico.administracionusuarios.dto.UsuarioRequestDTO;
import com.centromedico.administracionusuarios.dto.UsuarioResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "servicio-usuarios", url = "${usuarios.service.url}")
public interface UsuarioClient {

    @GetMapping("/api/usuarios/admin")
    List<UsuarioResponseDTO> obtenerTodos();

    @GetMapping("/api/usuarios/admin{id}")
    UsuarioResponseDTO obtenerPorId(@PathVariable("id") Long id);

    @PostMapping("/api/usuarios/admin")
    UsuarioResponseDTO crear(@RequestBody UsuarioRequestDTO dto);

    @PutMapping("/api/usuarios/admin")
    UsuarioResponseDTO actualizar(@PathVariable("id") Long id, @RequestBody UsuarioRequestDTO dto);

    @DeleteMapping("/api/usuarios/admin/{id}")
    void eliminar(@PathVariable("id") Long id);
}
