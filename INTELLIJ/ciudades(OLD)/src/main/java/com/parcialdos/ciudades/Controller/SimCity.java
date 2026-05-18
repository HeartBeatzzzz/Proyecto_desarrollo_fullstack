package com.parcialdos.ciudades.Controller;

import com.parcialdos.ciudades.Model.Ciudad;
import com.parcialdos.ciudades.Service.CiudadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/api/ciudad")
public class SimCity {
    @Autowired
    private CiudadService Municipalidad;

@GetMapping
public ResponseEntity<List<Ciudad>> listarciudades() {
        List<Ciudad> ciudades = Municipalidad.findall();
        if (ciudades.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ciudades);
    };
}
