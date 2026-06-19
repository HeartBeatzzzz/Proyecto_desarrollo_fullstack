package com.reservas.reservas.repository;

import com.reservas.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByIdUsuario(Long idUsuario);

    List<Reserva> findByIdEmp(Long idEmp);

    List<Reserva> findByDisponibilidadCita(String disponibilidadCita);


}
