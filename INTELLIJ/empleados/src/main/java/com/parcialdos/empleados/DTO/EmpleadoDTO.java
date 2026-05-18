package com.parcialdos.empleados.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDTO {
private Long id_emp;
    private String nombre_emp;
    private String especialidad;
    private String telefono_emp;
    private String email_emp;
}
