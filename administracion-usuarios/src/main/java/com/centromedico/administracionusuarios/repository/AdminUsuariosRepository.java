package com.centromedico.administracionusuarios.repository;

import com.centromedico.administracionusuarios.model.AdminUsuarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUsuariosRepository extends JpaRepository<AdminUsuarios, Long> {
}
