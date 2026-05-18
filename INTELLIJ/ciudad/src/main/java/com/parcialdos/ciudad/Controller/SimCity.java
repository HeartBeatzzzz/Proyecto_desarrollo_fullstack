package com.parcialdos.ciudad.Controller;

import com.parcialdos.ciudad.CiudadApplication;
import com.parcialdos.ciudad.Model.Ciudad;
import com.parcialdos.ciudad.Service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ciudades")
public class SimCity {
    @Autowired
    private CityService Municipalidad;

    @GetMapping
    public ResponseEntity<List<Ciudad>> FindAll()
    {
        List<Ciudad> Ciudades= Municipalidad.findall();
        if (Ciudades.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(Ciudades);};
    @PostMapping
    public ResponseEntity<List<Ciudad>> Save()
    {
        if (Ciudades.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(Ciudades);};
}
