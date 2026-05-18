package com.parcialdos.ciudad.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "ciudades")
@AllArgsConstructor
@NoArgsConstructor
public class Ciudad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CiudadId;
    @Column(nullable = false, length=255)
    private String nombre_ciudad;
    @Column(nullable = false, length=255)
    private String comuna;
}
