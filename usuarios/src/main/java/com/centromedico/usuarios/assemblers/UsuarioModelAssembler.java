package com.centromedico.usuarios.assemblers;

import com.centromedico.usuarios.controller.UsuarioCitaController;
import com.centromedico.usuarios.controller.UsuarioController;
import com.centromedico.usuarios.dto.UsuarioResponseDTO;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler {

    public UsuarioResponseDTO toModel(UsuarioResponseDTO usuario, boolean admin) {
        //enlace self (según el rol)
        Link selfLink;
        if (admin) {
            selfLink = linkTo(methodOn(UsuarioController.class)
                    .obtenerPorIdAdmin(usuario.getIdUsuario())).withSelfRel();
        } else {
            selfLink = linkTo(methodOn(UsuarioController.class)
                    .obtenerPorId(usuario.getIdUsuario())).withSelfRel();
        }
        usuario.add(selfLink);

        //enlace a las citas
        usuario.add(linkTo(methodOn(UsuarioCitaController.class)
                .obtenerCita(usuario.getIdUsuario())).withRel("citas"));

        return usuario;
    }
}