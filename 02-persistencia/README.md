# Fase 2: Persistencia

Esta fase parte de lo que ya se ha trabajado en `01-no-persistencia-ssr` y de los conocimientos previos de bases de datos relacionales, SQL, JDBC, DAO, Singleton e inyección de dependencias. No pretende volver a impartir esos contenidos: el foco nuevo es el mapeo objeto-relacional y su uso en una aplicación Spring Boot.

## Índice de trabajo

1. **Introducción al ORM y al mapeo objeto-relacional** — conceptos, vocabulario y papel de JPA, Hibernate y Spring Data JPA. Material: [P1 ORM](p1-orm/01-introduccion-orm.md).
2. **Primer modelo sencillo, sin relaciones** — introducir una clase propia y sus objetos, diferenciarlos de las colecciones simples y del `Model` de Spring MVC. Aplicación: [P2 Primer modelo](p2-PrimerModelo/README.md).
3. **Configuración de persistencia e Hibernate** — conexión de la aplicación a una base de datos local y opciones de gestión del esquema. Material: [guía de configuración de Hibernate](p1-orm/02-configuracion-hibernate.md).
4. **Operaciones de persistencia y consultas iniciales** — repositorios, operaciones básicas y formas de expresar consultas. Se estudian juntas para que las consultas se entiendan en el contexto de la recuperación y modificación de datos.
5. **Integración web, validación y reglas de la aplicación** — conectar operaciones reales con las vistas y formularios que hagan falta; separar los datos de entrada de las entidades y situar las validaciones y reglas en la capa apropiada.
6. **Segundo modelo: relaciones y estructuras más complejas** — después de conocer el mapeo básico y las consultas iniciales, ampliar el modelo con relaciones entre entidades.
7. **Transacciones** — límites de una unidad de trabajo y consistencia. Contenido avanzado, a desarrollar cuando la base de persistencia esté asentada.
8. **Ampliaciones** — pruebas de persistencia, rendimiento y diagnóstico, y evolución/migración del esquema. Se tratarán como prácticas o extensiones diferenciadas, no como requisitos de entrada.

El orden es una propuesta de organización, no un enunciado cerrado. En especial, “modelo” puede referirse al modelo del dominio, a una entidad persistente, al DTO de entrada o al `Model` MVC: las guías distinguen estos términos para evitar que se mezclen.

## Material y proyectos disponibles

- `p1-orm`: guías de estudio para el alumnado; la presentación se añadirá aparte.
- `p2-PrimerModelo`: aplicación Spring Boot ejecutable que presenta objetos de un modelo sencillo en memoria. No utiliza ORM ni persiste información.

Las guías no prescriben un único motor de base de datos. Cada estudiante podrá utilizar una base de datos relacional disponible localmente; los ejemplos de clase podrán elegir un motor de referencia y señalar sus diferencias cuando importen.

La configuración real de Hibernate y la conexión a una base de datos se abordarán después de conocer el primer modelo.
