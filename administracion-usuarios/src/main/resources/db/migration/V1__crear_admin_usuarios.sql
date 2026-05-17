CREATE TABLE admin_usuarios (
    id_auditoria BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    accion VARCHAR(50) NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    admin_responsable VARCHAR(100) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_admin_usuarios_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios_db.usuarios(id_usuario),
    INDEX idx_admin_usuarios_id_usuario (id_usuario)
);
