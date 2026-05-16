package com.centromedico.usuarios.controller;

import com.centromedico.usuarios.dto.UsuarioResponseDTO;
import com.centromedico.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Valid
@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Aquí puedes agregar otros endpoints CRUD si los necesitas:
    // @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
}

    //Hacerlo void permite especificar datos sin retornar necesariamente un body
    /*
    RESERVADO PARA ADMINISTRACION
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar (@PathVariable Long id){
        if (usuarioService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }*/



