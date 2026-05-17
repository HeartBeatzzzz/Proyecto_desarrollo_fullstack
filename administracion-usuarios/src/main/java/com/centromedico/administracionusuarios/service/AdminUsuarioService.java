package com.centromedico.administracionusuarios.service;

import com.centromedico.administracionusuarios.client.UsuarioClient;
import com.centromedico.administracionusuarios.dto.UsuarioRequestDTO;
import com.centromedico.administracionusuarios.dto.UsuarioResponseDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminUsuarioService {

    private final UsuarioClient usuarioClient;

    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioClient.obtenerTodos();
    }

    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        try {
            return Optional.of(usuarioClient.obtenerPorId(id));
        } catch (FeignException.NotFound ex) {
            return Optional.empty();
        }
    }

    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        return usuarioClient.crear(dto);
    }

    public Optional<UsuarioResponseDTO> actualizar(Long id, UsuarioRequestDTO dto) {
        try {
            return Optional.of(usuarioClient.actualizar(id, dto));
        } catch (FeignException.NotFound ex) {
            return Optional.empty();
        }
    }

    public boolean eliminar(Long id) {
        try {
            usuarioClient.eliminar(id);
            return true;
        } catch (FeignException.NotFound ex) {
            return false;
        }
    }
}
