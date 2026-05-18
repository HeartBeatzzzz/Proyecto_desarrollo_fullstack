CREATE TABLE empleados(
id_emp BIGINT AUTO_INCREMENT,
rut_emp VARCHAR(10) NOT NULL,
nombre_emp VARCHAR(255) NOT NULL,
especialidad VARCHAR(255) NOT NULL,
id_ciudad BIGINT NOT NULL,
telefono_emp VARCHAR(10),
email_emp VARCHAR(255) NOT NULL,
CONSTRAINT pk_id_emp PRIMARY KEY(id_emp)
);
