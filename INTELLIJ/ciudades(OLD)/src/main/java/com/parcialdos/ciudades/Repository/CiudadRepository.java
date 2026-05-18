package com.parcialdos.ciudades.Repository;

import com.parcialdos.ciudades.Model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad,Long> {
}
