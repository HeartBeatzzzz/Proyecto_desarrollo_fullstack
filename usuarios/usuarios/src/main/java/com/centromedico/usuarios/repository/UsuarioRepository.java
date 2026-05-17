package com.centromedico.usuarios.repository;

import com.centromedico.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findAllByActivoTrue();

    Optional<Usuario> findByIdUsuarioAndActivoTrue(Long idUsuario);
}
