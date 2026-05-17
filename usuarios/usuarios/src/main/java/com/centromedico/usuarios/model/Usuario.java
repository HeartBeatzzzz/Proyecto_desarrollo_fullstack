package com.centromedico.usuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "RUT_USUARIO", nullable = false, length = 10)
    private String rutUsuario;

    // La relación real la gestiona el microservicio de Reservas.
    @Column(name = "ID_CITA", nullable = true)
    private Long idCita;

    @Column(name = "NOMBRE_USUARIO", nullable = false, length = 100)
    private String nombreUsuario;

    @Column(name = "FECHA_NAC_USUARIO", nullable = false)
    private LocalDate fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "ID_CIUDAD", nullable = false)
    private Ciudad idCiudad;

    @Column(name = "TELEFONO_USUARIO", nullable = true, length = 20)
    private String telefonoUsuario;

    @Column(name = "EMAIL_USUARIO", nullable = false, length = 100)
    private String emailUsuario;

    @Column(name = "ACTIVO", nullable = false)
    private Boolean activo;
}
