package com.centromedico.administracionusuarios.service;

import com.centromedico.administracionusuarios.client.UsuarioClient;
import com.centromedico.administracionusuarios.dto.UsuarioRequestDTO;
import com.centromedico.administracionusuarios.dto.UsuarioResponseDTO;
import com.centromedico.administracionusuarios.model.AdminUsuarios;
import com.centromedico.administracionusuarios.repository.AdminUsuariosRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUsuarioService {

    private static final String ADMIN_RESPONSABLE = "servicio-administracion-usuarios";

    private final UsuarioClient usuarioClient;
    private final AdminUsuariosRepository adminUsuariosRepository;

    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioClient.obtenerTodos();
    }

    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        try {
            UsuarioResponseDTO usuario = usuarioClient.obtenerPorId(id);
            registrarAuditoria(id, "CONSULTA", "Consulta de usuario por ID desde administracion");
            return Optional.of(usuario);
        } catch (FeignException.NotFound ex) {
            return Optional.empty();
        }
    }

    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        UsuarioResponseDTO usuarioCreado = usuarioClient.crear(dto);
        registrarAuditoria(usuarioCreado.getIdUsuario(), "CREACION", "Creacion de usuario desde administracion");
        return usuarioCreado;
    }

    public Optional<UsuarioResponseDTO> actualizar(Long id, UsuarioRequestDTO dto) {
        try {
            UsuarioResponseDTO usuarioActualizado = usuarioClient.actualizar(id, dto);
            registrarAuditoria(id, "ACTUALIZACION", "Actualizacion de usuario desde administracion");
            return Optional.of(usuarioActualizado);
        } catch (FeignException.NotFound ex) {
            return Optional.empty();
        }
    }

    public boolean eliminar(Long id) {
        try {
            usuarioClient.eliminar(id);
            registrarAuditoria(id, "ELIMINACION", "Eliminacion de usuario desde administracion");
            return true;
        } catch (FeignException.NotFound ex) {
            return false;
        }
    }

    private void registrarAuditoria(Long idUsuario, String accion, String motivo) {
        try {
            AdminUsuarios auditoria = new AdminUsuarios();
            auditoria.setIdUsuario(idUsuario);
            auditoria.setAccion(accion);
            auditoria.setMotivo(motivo);
            auditoria.setAdminResponsable(ADMIN_RESPONSABLE);
            adminUsuariosRepository.save(auditoria);
        } catch (RuntimeException ex) {
            log.warn("No se pudo registrar la auditoria para el usuario {} y accion {}: {}", idUsuario, accion, ex.getMessage());
        }
    }
}
