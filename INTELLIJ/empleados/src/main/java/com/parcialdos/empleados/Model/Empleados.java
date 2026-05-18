package com.parcialdos.empleados.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "empleados")
@AllArgsConstructor
@NoArgsConstructor
public class Empleados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_emp;
    @Column(nullable = false)
    private String rut_emp;
    @Column(nullable = false)
    private String nombre_emp;
    @Column(nullable = false)
    private String especialidad;
    @Column(nullable = false)
    private Long id_ciudad;
    @Column(nullable = true)
    private String telefono_emp;
    @Column(nullable = false)
    private String email_emp;

}
