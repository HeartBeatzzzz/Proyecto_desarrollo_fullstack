package com.centromedico.usuarios.repository;

import com.centromedico.usuarios.model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;

//Repository de ciudad para poder insertar en la tabla usuario
public interface CiudadRepository extends JpaRepository<Ciudad, Long> { }