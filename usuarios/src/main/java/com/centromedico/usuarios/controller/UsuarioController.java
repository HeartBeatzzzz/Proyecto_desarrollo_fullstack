package com.centromedico.usuarios.controller;

import com.centromedico.usuarios.assemblers.UsuarioModelAssembler;
import com.centromedico.usuarios.dto.UsuarioRequestDTO;
import com.centromedico.usuarios.dto.UsuarioResponseDTO;
import com.centromedico.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Valid
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler assembler;

    @GetMapping("/admin")
    public ResponseEntity<CollectionModel<UsuarioResponseDTO>> listarTodosParaAdmin() {
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerTodos().stream()
                .map(usuario -> assembler.toModel(usuario, true))
                .toList();

        CollectionModel<UsuarioResponseDTO> coleccion = CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioController.class).listarTodosParaAdmin()).withSelfRel()
        );
        return ResponseEntity.ok(coleccion);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorIdAdmin(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(usuario -> ResponseEntity.ok(assembler.toModel(usuario, true)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(usuario -> ResponseEntity.ok(assembler.toModel(usuario, false)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/admin")
    public ResponseEntity<UsuarioResponseDTO> crearAdmin(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO creado = usuarioService.crear(dto);
        return ResponseEntity.status(201).body(assembler.toModel(creado, true));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO creado = usuarioService.crear(dto);
        return ResponseEntity.status(201).body(assembler.toModel(creado, false));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.actualizar(id, dto)
                .map(usuario -> ResponseEntity.ok(assembler.toModel(usuario, true)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!usuarioService.eliminar(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}