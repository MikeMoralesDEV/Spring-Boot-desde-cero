# Apartado 2 — Del SQL al modelo Java

## Objetivo

Leer un esquema relacional SQL proporcionado y crear dos entidades JPA que se adapten exactamente a sus tablas y columnas. En este apartado el SQL es la fuente del diseño; Hibernate no debe alterarlo.

No se implementan consultas, formularios ni operaciones CRUD. No hay relaciones entre las dos tablas.

## Contexto

El aula maker ha definido un esquema inicial para guardar materiales y puestos de trabajo. El esquema está escrito en `src/main/resources/schema.sql`. Tu tarea es representar ese esquema mediante dos entidades Java y su mapeo JPA.

## Trabajo

1. Lee el archivo `src/main/resources/schema.sql` y anota para cada tabla sus columnas, tipos, nulabilidad, clave primaria y restricciones.
2. Crea las entidades `MaterialAula` y `PuestoTrabajo` en el paquete indicado por el proyecto.
3. Mapea exactamente los nombres de las tablas y columnas. En especial, usa `@Column(name = "...")` cuando el nombre SQL no coincida con el nombre Java.
4. Representa correctamente tipos numéricos, texto, booleanos y valores nulos.
5. Aplica Lombok con las anotaciones necesarias, incluido el constructor sin argumentos de las entidades.
6. Documenta las entidades con comentarios breves que expliquen su propósito y cualquier decisión de mapeo que no resulte evidente. No comentes línea por línea ni repitas literalmente lo que ya expresan las anotaciones.
7. No cambies el SQL ni crees tablas adicionales.
8. Completa `spring.jpa.hibernate.ddl-auto` con `validate`. Hibernate debe comprobar el mapeo sin modificar las tablas creadas por el script.
9. Ejecuta las pruebas y abre el informe HTML.

## Esquema entregado

El archivo `src/main/resources/schema.sql` define exactamente estas dos tablas:

- `material_aula`: material individual con identificador asignado por el sistema externo, referencia, nombre, descripción, coste de reposición y unidades.
- `puesto_trabajo`: puesto del aula con identificador asignado por el sistema externo, código, denominación, planta e indicador de alimentación eléctrica.

Cada tabla tiene una clave primaria independiente. No hay claves foráneas, relaciones ni columnas que apunten a la otra tabla.

La referencia del material y el código del puesto son únicos y obligatorios. El identificador se proporciona desde fuera: no añadas generación automática de valores en JPA.

## Informe HTML de pruebas

Desde esta carpeta:

```powershell
.\mvnw.cmd clean test "-Dmaven.test.failure.ignore=true" surefire-report:report-only
```

Abre `target\reports\surefire.html`. Las pruebas comprueban que el modelo mapea las tablas y columnas entregadas y que Hibernate puede validar el esquema. Si el mapeo no coincide, la validación de Hibernate y/o las pruebas aparecerán como fallidas.

## Cuidado con el esquema

El script usa `CREATE TABLE IF NOT EXISTS`, por lo que puede ejecutarse más de una vez sin borrar tablas. El modo `validate` no crea, modifica ni elimina tablas. Si se usa otra base de datos local, adapta la conexión y, si el motor requiere una variación sintáctica, conserva el diseño relacional definido por el script.

## No se pide

- Añadir relaciones, claves foráneas o colecciones de entidades.
- Rehacer o mejorar el diseño SQL.
- Repositorios, DAO, servicios, controladores o formularios.
- Insertar datos o desarrollar operaciones CRUD.
