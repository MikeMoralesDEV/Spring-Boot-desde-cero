CREATE TABLE IF NOT EXISTS material_aula (
    id BIGINT NOT NULL PRIMARY KEY,
    referencia VARCHAR(12) NOT NULL UNIQUE,
    nombre_material VARCHAR(80) NOT NULL,
    descripcion VARCHAR(250),
    coste_reposicion DECIMAL(8, 2) NOT NULL,
    unidades SMALLINT NOT NULL
);

CREATE TABLE IF NOT EXISTS puesto_trabajo (
    id BIGINT NOT NULL PRIMARY KEY,
    codigo_puesto VARCHAR(10) NOT NULL UNIQUE,
    denominacion VARCHAR(60) NOT NULL,
    planta INTEGER NOT NULL,
    tiene_electricidad BOOLEAN NOT NULL
);
