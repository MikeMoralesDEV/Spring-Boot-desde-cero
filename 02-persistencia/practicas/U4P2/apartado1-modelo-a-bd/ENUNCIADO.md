# Apartado 1 — Del diseño formal a la base de datos

## Objetivo

Partir de una especificación de requisitos y de un diseño relacional formal para crear dos entidades JPA. Al arrancar la aplicación, Hibernate debe generar las dos tablas.

En este apartado se evalúa el **mapeo de dos entidades sin relaciones**. No hay que implementar consultas, formularios ni operaciones CRUD.

## Contexto

Un aula maker quiere registrar los equipos que presta y los espacios de trabajo disponibles. Son inventarios independientes para esta práctica: **no se relacionan entre sí**, no se incluyen claves foráneas y no se requiere que una fila de una tabla haga referencia a la otra.

## Requisitos de la entidad `Recurso`

Cada recurso tiene:

- Un identificador numérico generado por la base de datos.
- Un código único de hasta 12 caracteres, obligatorio.
- Un nombre de hasta 80 caracteres, obligatorio.
- Una descripción de hasta 250 caracteres, opcional.
- Un coste de reposición con hasta 8 dígitos en total y 2 decimales, obligatorio.
- Un indicador de si se puede prestar, obligatorio.

## Requisitos de la entidad `Espacio`

Cada espacio tiene:

- Un identificador numérico generado por la base de datos.
- Un código único de hasta 8 caracteres, obligatorio.
- Un nombre de hasta 60 caracteres, obligatorio.
- Un aforo entero, obligatorio.
- Una planta entera, obligatoria.
- Un indicador de accesibilidad, obligatorio.

## Diseño relacional que debe producirse

Usa este diseño como contrato para el mapeo. Los nombres de tabla y columna son explícitos y deben coincidir.

### Tabla `recurso`

| Columna | Tipo esperado | Nulo | Restricciones |
|---|---|---:|---|
| `id` | BIGINT | No | Clave primaria; generación automática |
| `codigo` | VARCHAR(12) | No | Único |
| `nombre` | VARCHAR(80) | No | — |
| `descripcion` | VARCHAR(250) | Sí | — |
| `coste_reposicion` | DECIMAL(8,2) | No | Precisión 8, escala 2 |
| `prestable` | BOOLEAN | No | — |

### Tabla `espacio`

| Columna | Tipo esperado | Nulo | Restricciones |
|---|---|---:|---|
| `id` | BIGINT | No | Clave primaria; generación automática |
| `codigo_espacio` | VARCHAR(8) | No | Único |
| `nombre_espacio` | VARCHAR(60) | No | — |
| `aforo` | INTEGER | No | — |
| `planta` | INTEGER | No | — |
| `accesible` | BOOLEAN | No | — |

### Reglas generales

- El modelo contiene exactamente estas dos entidades y estas dos tablas.
- No crees relaciones entre las entidades ni claves foráneas.
- La clave primaria de cada tabla se llama `id` y se genera automáticamente.
- Los nombres SQL son los de las tablas; no dependas de que Hibernate invente una estrategia de nombres.
- Los requisitos de longitud, nulabilidad, unicidad y precisión deben expresarse en el mapeo ORM.
- La práctica utiliza Lombok para evitar escribir a mano el código repetitivo de las entidades. El constructor sin argumentos requerido por JPA debe estar presente, generado por Lombok.
- Documenta las entidades con comentarios breves que expliquen su propósito y cualquier decisión de mapeo que no resulte evidente. No comentes línea por línea ni repitas literalmente lo que ya expresan las anotaciones.

## Trabajo

1. Revisa la estructura del proyecto Spring Boot proporcionado.
2. Crea `Recurso` y `Espacio` como entidades JPA en el paquete indicado por las instrucciones del proyecto.
3. Mapea cada entidad a la tabla y columnas indicadas.
4. Aplica las opciones de `@Column` necesarias para representar longitud, nulabilidad, unicidad, precisión y escala. Usa nombres explícitos cuando el diseño indique un nombre SQL.
5. Configura Lombok con las anotaciones necesarias. No añadas `@Data` automáticamente: considera qué métodos genera además de getters y setters.
6. Completa la propiedad `spring.jpa.hibernate.ddl-auto` de la plantilla con `create`.
7. Ejecuta las pruebas del proyecto y revisa el informe HTML.
8. Comprueba en los metadatos de la base de datos que existen ambas tablas con sus columnas y restricciones.
9. Añade documentación breve y útil al código de las entidades, como se indica en las reglas generales.

## Informe HTML de pruebas

Desde esta carpeta:

```powershell
.\mvnw.cmd clean test "-Dmaven.test.failure.ignore=true" surefire-report:report-only
```

Abre `target\reports\surefire.html`. El informe muestra el resultado de las comprobaciones del modelo y del esquema. No basta con que la aplicación compile: todas las pruebas deben aparecer como correctas.

## Cuidado con los datos

`create` genera el esquema desde las entidades al arrancar y puede eliminar tablas anteriores. Usa una base local de práctica sin información que quieras conservar.

## No se pide

- Relaciones entre tablas, claves foráneas ni colecciones de entidades.
- Repositorios, DAO, servicios, controladores o formularios.
- Insertar registros o implementar operaciones CRUD.
- Cambiar el diseño especificado.
