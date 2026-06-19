--Crear tabla de auditoría para acciones administrativas sobre usuarios
CREATE TABLE admin_usuarios (
    id_auditoria BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    accion VARCHAR(50) NOT NULL
    motivo VARCHAR(255)
    admin_responsable VARCHAR(100) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_usuario (id_usuario)
);