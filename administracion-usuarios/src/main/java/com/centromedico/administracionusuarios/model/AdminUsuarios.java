package com.centromedico.administracionusuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admin_usuarios")
public class AdminUsuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Long idAuditoria;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @NotBlank(message = "La accion es obligatoria")
    @Column(name = "accion", nullable = false, length = 50)
    private String accion;

    @NotBlank(message = "El motivo es obligatorio")
    @Column(name = "motivo", nullable = false, length = 255)
    private String motivo;

    @NotBlank(message = "El admin responsable es obligatorio")
    @Column(name = "admin_responsable", nullable = false, length = 100)
    private String adminResponsable;

    @Column(name = "fecha", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fecha;
}
