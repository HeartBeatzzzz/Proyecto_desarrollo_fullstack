package com.reservas.reservas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Data
@Table(name = "cita")
@NoArgsConstructor
@AllArgsConstructor

public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_cita")
    private Long idCita;

    @Column(name = "tipo_cita", nullable = false, length = 100)
    private String tipoCita;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDate fechaCita;

    @Column(name = "rut_usuario", nullable = false, length = 10)
    private Long idUsuario;

    @Column(name = "rut_emp", nullable = false, length = 10)
    private Long idEmp;

    @Column(name = "disponibilidad_cita", nullable = false, length = 50)
    private String disponibilidadCita;

}
