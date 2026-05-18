package com.parcialdos.empleados.Controller;

import com.parcialdos.empleados.DTO.EmpleadoDTO;
import com.parcialdos.empleados.Model.Empleados;
import com.parcialdos.empleados.Service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/emp")
public class TheBoss {
    @Autowired
    private EmpleadoService EMPSERV;
@GetMapping
    public ResponseEntity<List<EmpleadoDTO>> listarempleados(){
  List<EmpleadoDTO> empleados=  EMPSERV.findAll();
  if(empleados.isEmpty()){
      return ResponseEntity.noContent().build();
  }
  return ResponseEntity.ok(empleados);
};
@PostMapping
    public EmpleadoDTO agregarempleado(@RequestBody Empleados Esclavo){

    EMPSERV.save(Esclavo);
    return new EmpleadoDTO(Esclavo.getId_emp(), Esclavo.getNombre_emp(), Esclavo.getEspecialidad(), Esclavo.getTelefono_emp(), Esclavo.getEmail_emp());
}
}
