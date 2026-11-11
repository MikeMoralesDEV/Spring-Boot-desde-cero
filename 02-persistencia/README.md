# Fase 2: Persistencia

Esta fase parte de lo que ya se ha trabajado en `01-no-persistencia-ssr` y de los conocimientos previos de bases de datos relacionales, SQL, JDBC, DAO, Singleton e inyección de dependencias. No pretende volver a impartir esos contenidos: el foco nuevo es el mapeo objeto-relacional y su uso en una aplicación Spring Boot.

## Índice de trabajo

1. **Introducción al ORM y al mapeo objeto-relacional** — conceptos, vocabulario y papel de JPA, Hibernate y Spring Data JPA. Material: [P1 ORM](p1-orm/01-introduccion-orm.md).
2. **Primer modelo sencillo, sin relaciones** — mapear una clase propia con Hibernate ORM y crear el esquema al iniciar. Sin servidor web, repositorios, servicios ni formularios. Ejemplo: [P2 Primer modelo](p2-PrimerModelo/README.md). Práctica: [U4P2](practicas/U4P2/README.md).
3. **Primera aplicación de persistencia** — conectar Spring Boot a MariaDB e integrar modelo, repositorio, servicio, controlador y vista en un caso completo, sin relaciones entre entidades. Material: [DriversBD](p3-inventario-videojuegos/DriversBD.md). Proyecto: [Inventario de videojuegos](p3-inventario-videojuegos/README.md).
4. **Operaciones de persistencia y consultas iniciales** — repositorios, operaciones básicas y formas de expresar consultas. Se estudian juntas para que las consultas se entiendan en el contexto de la recuperación y modificación de datos.
5. **Integración web, validación y reglas de la aplicación** — conectar operaciones reales con las vistas y formularios que hagan falta; separar los datos de entrada de las entidades y situar las validaciones y reglas en la capa apropiada.
6. **Segundo modelo: relaciones y estructuras más complejas** — después de conocer el mapeo básico y las consultas iniciales, ampliar el modelo con relaciones entre entidades.
7. **Transacciones** — límites de una unidad de trabajo y consistencia. Contenido avanzado, a desarrollar cuando la base de persistencia esté asentada.
8. **Ampliaciones** — pruebas de persistencia, rendimiento y diagnóstico, y evolución/migración del esquema. Se tratarán como prácticas o extensiones diferenciadas, no como requisitos de entrada.

El orden es una propuesta de organización, no un enunciado cerrado. En especial, “modelo” puede referirse al modelo del dominio, a una entidad persistente, al DTO de entrada o al `Model` MVC: las guías distinguen estos términos para evitar que se mezclen.

## Material y proyectos disponibles

- `p1-orm`: guías de estudio para el alumnado; la presentación se añadirá aparte.
- `p2-PrimerModelo`: aplicación Spring Boot mínima con una entidad Hibernate y creación automática del esquema en H2 local. No incluye capa web ni operaciones de datos.
- `practicas/U4P2`: práctica en dos apartados para generar el esquema desde un modelo formal y mapear entidades a partir de SQL.
- `p3-primera-aplicacion-persistencia`: material inicial sobre los drivers de bases de datos y la elección de MariaDB como motor de referencia.
- `p3-inventario-videojuegos`: aplicación completa para consultar y dar de alta juegos en el inventario de una tienda, con MariaDB.

MariaDB será el motor de referencia a partir de P3. MySQL y PostgreSQL se podrán comparar más adelante, cuando el flujo completo de persistencia ya sea conocido.

La configuración de Hibernate y los modos de gestión del esquema se introducen antes de P3; la guía correspondiente está en `p1-orm/02-configuracion-hibernate.md`.
