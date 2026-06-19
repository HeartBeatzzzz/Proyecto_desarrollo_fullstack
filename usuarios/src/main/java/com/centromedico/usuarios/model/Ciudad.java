package com.centromedico.usuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ciudad")
public class Ciudad
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Long id;

    @Column(name = "NOMBRE_CIUDAD", nullable = false, length = 255)
    private String nombreCiudad;

    @Column(name = "COMUNA", nullable = false, length = 100)
    private String comuna;
}