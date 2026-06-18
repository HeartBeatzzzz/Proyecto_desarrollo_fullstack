CREATE TABLE usuarios (
                         id_usuario BIGINT AUTO_INCREMENT,
                         rut_usuario VARCHAR(10) NOT NULL,
                         id_cita BIGINT NULL,
                         nombre_usuario VARCHAR(100) NOT NULL,
                         fecha_nac_usuario DATE NOT NULL,
                         id_ciudad BIGINT NOT NULL,
                         telefono_usuario VARCHAR(20),
                         email_usuario VARCHAR(100) NOT NULL,
                         CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
                         CONSTRAINT fk_usuario_ciudad FOREIGN KEY (id_ciudad)
                         REFERENCES ciudad(id_ciudad));