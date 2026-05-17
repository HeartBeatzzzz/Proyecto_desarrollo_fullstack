package com.centromedico.administracionusuarios.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admin_usuarios")
public class AdminUsuarios {
    @Id
    @Column(name = "id_usuario")
    private Long id; // id recibido del ms usuario

    @Column(name = "rut_usuario", length = 10, nullable = false)
    private String rutUsuario;

    @Column(name = "nombre_usuario", length = 100, nullable = false)
    private String nombreUsuario;

    @Column(name = "id_ciudad", nullable = false)
    private Long idCiudad;

    @Column(name = "telefono_usuario", nullable = true, length = 20)
    private String telefonoUsuario;

    @Column(name = "email_usuario", length = 100, nullable = false)
    private String emailUsuario;

    @Column(name = "estado_cuenta", length = 20)
    private String estadoCuenta;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacionLocal;

    @Column(name = "fecha_ultima_sincronizacion")
    private Date fechaSincronizacion;

    @Column(name = "eliminado", nullable = true)
    private boolean eliminado;
}
