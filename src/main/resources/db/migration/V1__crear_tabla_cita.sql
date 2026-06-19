CREATE TABLE IF NOT EXISTS cita (
    id_cita BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_cita VARCHAR(100) NOT NULL,
    fecha_cita DATE NOT NULL,
    rut_usuario BIGINT NOT NULL,
    rut_emp BIGINT NOT NULL,
    disponibilidad_cita VARCHAR(50) NOT NULL
    );