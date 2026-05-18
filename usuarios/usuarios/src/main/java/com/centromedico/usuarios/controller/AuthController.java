package com.centromedico.usuarios.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @GetMapping(value = "/login/usuario", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String loginUsuario() {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <title>Login Usuario</title>
                </head>
                <body>
                    <h1>Login de Usuario</h1>
                    <form method="post" action="/perform_login">
                        <label>Usuario</label><br>
                        <input type="text" name="username"><br><br>
                        <label>Contrasena</label><br>
                        <input type="password" name="password"><br><br>
                        <button type="submit">Ingresar</button>
                    </form>
                </body>
                </html>
                """;
    }

    @GetMapping(value = "/login/admin", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String loginAdmin() {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <title>Login Admin Usuarios</title>
                </head>
                <body>
                    <h1>Login de Administrador</h1>
                    <form method="post" action="/perform_login">
                        <label>Administrador</label><br>
                        <input type="text" name="username"><br><br>
                        <label>Contrasena</label><br>
                        <input type="password" name="password"><br><br>
                        <button type="submit">Ingresar</button>
                    </form>
                </body>
                </html>
                """;
    }
}
