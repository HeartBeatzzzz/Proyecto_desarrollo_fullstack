package com.centromedico.administracionusuarios.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Long idAuditoria;

    @NotBlank(message = "El ID del usuario no puede estar vacio")
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "La accion es obligatoria")
    @Column(name = "accion")
    private String accion;

    @NotBlank(message = "El motivo no puede estar vacio")
    @NotNull(message = "El motivo es obligatorio")
    @Column(name = "motivo")
    private String motivo;

    @NotNull(message = "El admin es obligatorio")
    @Column(name = "admin_responsable")
    private String adminResponsable;

    @NotNull(message = "La fecha no puede estar vacia")
    @Column(name = "fecha")
    private LocalDate fecha;
}
