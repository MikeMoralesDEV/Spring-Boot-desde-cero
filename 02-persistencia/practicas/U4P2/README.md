# U4P2 — Primer modelo y mapeo objeto-relacional

Práctica introductoria de ORM con dos apartados independientes. En ambos se trabaja con **dos tablas sin relaciones entre ellas**. No se piden controladores, páginas de negocio, formularios, repositorios, servicios ni operaciones CRUD.

## Apartados

1. **Del diseño formal a la base de datos.** A partir de una especificación de requisitos y un diseño relacional, crear dos entidades JPA. Hibernate generará las tablas al iniciar.
2. **Del SQL al modelo Java.** A partir de un script SQL proporcionado, crear dos entidades cuyo mapeo coincida con las tablas. El esquema lo crea el SQL y Hibernate comprueba el mapeo.

Cada apartado incluye un proyecto Spring Boot docente con pruebas JUnit. Tras ejecutar las pruebas, Maven genera un informe HTML con el resultado individual de cada comprobación. El informe no es una página de la aplicación: se genera como resultado de la ejecución de pruebas.

## Carpetas

- `apartado1-modelo-a-bd`: enunciado, plantilla de propiedades y proyecto de comprobación del primer apartado.
- `apartado2-sql-a-modelo`: enunciado, SQL entregado, plantilla de propiedades y proyecto de comprobación del segundo apartado.

## Cómo se ve el resultado de las pruebas

En la carpeta del proyecto correspondiente, ejecutar:

```powershell
.\mvnw.cmd clean test "-Dmaven.test.failure.ignore=true" surefire-report:report-only
```

Abrir `target\reports\surefire.html`. Cada prueba comprueba un aspecto concreto; el informe indica cuáles pasan y cuáles fallan. Si el arranque falla por el esquema o la configuración, también queda registrado como error en el informe.

## Advertencia sobre los modos de Hibernate

La propiedad `spring.jpa.hibernate.ddl-auto` queda intencionadamente vacía en los dos proyectos plantilla. Hay que completar el valor según el apartado antes de ejecutar la aplicación o las pruebas:

- Apartado 1: elegir `create`, porque el esquema debe generarse desde las entidades. Puede borrar y recrear tablas.
- Apartado 2: elegir `validate`, porque el SQL define el esquema y Hibernate debe comprobar que las entidades coinciden sin cambiarlo.

Usar una base o esquema local exclusivo para esta práctica. No ejecutar el apartado 1 contra una base con datos que se quieran conservar.

## Criterios de evaluación relacionados

La práctica aporta evidencias para los siguientes criterios de los resultados de aprendizaje RA5 y RA6. La cobertura se limita a lo que efectivamente se trabaja en U4P2; no se evalúan aquí contenidos excluidos como vistas, formularios, consultas o CRUD.

| RA y criterio | Cobertura en U4P2 | Evidencias observables |
|---|---|---|
| **RA5 a)** Separar lógica de negocio y presentación | Parcial e indirecta | Entidades persistentes aisladas del arranque y de las pruebas. Esta práctica no incluye controladores ni vistas, así que no demuestra una separación MVC completa. |
| **RA5 b)** Mecanismos y frameworks para separar presentación y lógica | Parcial e indirecta | Uso de Spring Boot, JPA y Hibernate para declarar el modelo persistente y generar o validar el esquema. No se implementa ni evalúa la separación mediante vistas y controladores. |
| **RA5 e)** Parámetros de configuración de la aplicación | Directa | Configuración de la conexión a la base de datos y selección razonada de `spring.jpa.hibernate.ddl-auto`: `create` en el apartado 1 y `validate` en el apartado 2. |
| **RA5 g)** Principios y patrones de diseño orientado a objetos | Limitada al alcance | Entidades Java con responsabilidades de mapeo claras, tipos adecuados y encapsulación; uso de anotaciones JPA y Lombok sin añadir capas que no pide la práctica. No se exige aplicar patrones de arquitectura no impartidos. |
| **RA5 h)** Probar y documentar el código | Directa | Código documentado con comentarios breves y pertinentes; ejecución de las pruebas y revisión del informe HTML de Surefire. |
| **RA6 a)** Tecnologías de acceso a almacenes de datos | Directa | Identificación y uso de JPA como especificación ORM, Hibernate como proveedor y JDBC como mecanismo subyacente de conexión. |
| **RA6 b)** Establecer conexiones con bases de datos | Directa | Configuración de URL, usuario y contraseña, e inicio de la aplicación conectada a H2. |
| **RA6 e)** Utilizar conjuntos de datos para almacenar información | Directa en el modelo | Representación de las tablas y columnas mediante entidades persistentes; en esta práctica no se insertan ni consultan registros. |
| **RA6 g)** Probar y documentar aplicaciones web | Parcial: se prueban y documentan las aplicaciones, pero no se desarrollan funcionalidades web | Pruebas automatizadas del modelo y del esquema, informe HTML y documentación del código y de la ejecución. |

**No se evalúan en U4P2:** RA5 c), d) y f), ni RA6 c), d) y f), porque no se generan vistas, no se usan formularios, no se mantiene estado web, no se recuperan ni publican registros y no se implementan actualizaciones o eliminaciones.
