CREATE TABLE ciudad (
                        id_ciudad BIGINT AUTO_INCREMENT,
                        nombre_ciudad VARCHAR(255) NOT NULL,
                        comuna VARCHAR(100) NOT NULL,
                        CONSTRAINT pk_ciudad PRIMARY KEY (id_ciudad));

