package com.centromedico.usuarios.service;

import com.centromedico.usuarios.client.CitaClient;
import com.centromedico.usuarios.dto.*;
import com.centromedico.usuarios.model.Ciudad;
import com.centromedico.usuarios.model.Usuario;
import com.centromedico.usuarios.repository.CiudadRepository;
import com.centromedico.usuarios.repository.UsuarioRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CiudadRepository ciudadRepository;
    private final CitaClient citaClient;

    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepository.findAllByActivoTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        return usuarioRepository.findByIdUsuarioAndActivoTrue(id)
                .map(this::mapToDTO);
    }

    @Transactional
    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        aplicarCambios(usuario, dto);
        usuario.setActivo(true);
        return mapToDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public Optional<UsuarioResponseDTO> actualizar(Long id, UsuarioRequestDTO dto) {
        return usuarioRepository.findByIdUsuarioAndActivoTrue(id)
                .map(usuario -> {
                    aplicarCambios(usuario, dto);
                    return mapToDTO(usuarioRepository.save(usuario));
                });
    }

    @Transactional
    public boolean eliminar(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByIdUsuarioAndActivoTrue(id);
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        return true;
    }

    /**
     * Agenda una cita para el usuario especificado.
     *  idUsuario ID del usuario (desde header X-User-Id)
     * request DTO con datos de la cita (idEmp, fechaCita, tipoCita)
     */

    // En UsuarioService.java

    //Obtiene la cita asociada al usuario (campo idCita de la entidad Usuario).

    public CitaResponseDTO obtenerCitaPorUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findByIdUsuarioAndActivoTrue(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (usuario.getIdCita() == null) {
            return null;
        }
        try {
            return citaClient.obtenerCitaPorId(usuario.getIdCita());
        } catch (FeignException.NotFound e) {
            log.warn("La cita con ID {} no existe en el microservicio de citas", usuario.getIdCita());
            return null;
        }
    }

    @Transactional
    public CitaResponseDTO agendarCita(Long idUsuario, CitaCreacionRequestDTO request) {
        log.info("Usuario {} intenta agendar una cita", idUsuario);

        Usuario usuario = usuarioRepository.findByIdUsuarioAndActivoTrue(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        // Inyectar el ID del usuario en el DTO (el cliente no lo envió)
        request.setIdUsuario(idUsuario);
        log.debug("Enviando solicitud de creación de cita al microservicio de citas: {}", request);
        CitaResponseDTO nuevaCita = citaClient.crearCita(request);

        // Guardar el ID de la cita en la entidad Usuario
        usuario.setIdCita(nuevaCita.getIdCita());
        usuarioRepository.save(usuario);

        log.info("Cita creada exitosamente con ID: {} para usuario: {}", nuevaCita.getIdCita(), idUsuario);
        return nuevaCita;
    }

    /**
     * Cancela una cita existente para el usuario especificado.
     */
    @Transactional
    public CitaResponseDTO cancelarCita(Long idUsuario, Long idCita, CitaCancelacionRequestDTO request) {
        log.info("Usuario {} intenta cancelar la cita {}", idUsuario, idCita);

        Usuario usuario = usuarioRepository.findByIdUsuarioAndActivoTrue(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        // Verificar que la cita pertenezca al usuario
        if (usuario.getIdCita() == null || !usuario.getIdCita().equals(idCita)) {
            throw new RuntimeException("La cita con ID " + idCita + " no pertenece al usuario " + idUsuario);
        }

        // Preparar DTO de cancelación
        if (request == null) {
            request = new CitaCancelacionRequestDTO();
        }
        request.setIdCita(idCita);
        request.setIdUsuario(idUsuario);

        log.debug("Enviando solicitud de cancelación al microservicio de citas: {}", request);
        CitaResponseDTO citaCancelada = citaClient.cancelarCita(idCita, request);

        // Si la cancelación fue exitosa, limpiar la referencia local
        if (citaCancelada != null && "CANCELADA".equals(citaCancelada.getDisponibilidadCita())) {
            usuario.setIdCita(null);
            usuarioRepository.save(usuario);
            log.info("Cita {} cancelada exitosamente para usuario {}", idCita, idUsuario);
        } else {
            log.warn("La cita {} no se canceló correctamente. Estado devuelto: {}",
                    idCita, citaCancelada != null ? citaCancelada.getDisponibilidadCita() : "null");
        }

        return citaCancelada;
    }
    /**
     * Convierte una entidad Usuario a UsuarioResponseDTO.
     */
    private UsuarioResponseDTO mapToDTO(Usuario u) {
        return new UsuarioResponseDTO(
                u.getIdUsuario(),
                u.getRutUsuario(),
                u.getIdCita(),
                u.getNombreUsuario(),
                u.getFechaNacimiento(),
                u.getIdCiudad().getId(),
                u.getTelefonoUsuario(),
                u.getEmailUsuario()
        );
    }

    private void aplicarCambios(Usuario usuario, UsuarioRequestDTO dto) {
        Ciudad ciudad = ciudadRepository.findById(dto.getIdCiudad())
                .orElseThrow(() -> new RuntimeException("La ciudad con ID " + dto.getIdCiudad() + " no existe"));

        usuario.setRutUsuario(dto.getRutUsuario());
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setIdCiudad(ciudad);
        usuario.setTelefonoUsuario(dto.getTelefonoUsuario());
        usuario.setEmailUsuario(dto.getEmailUsuario());
    }
}
