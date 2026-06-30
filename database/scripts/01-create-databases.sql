-- Las tablas las crea Hibernate con ddl-auto: update al arrancar cada microservicio
-- Los datos iniciales se insertan via data.sql en cada servicio (spring.jpa.defer-datasource-initialization: true)

CREATE DATABASE tdis_usuarios;
CREATE DATABASE tdis_catalogo;
CREATE DATABASE tdis_tramites;
CREATE DATABASE tdis_documentos;
