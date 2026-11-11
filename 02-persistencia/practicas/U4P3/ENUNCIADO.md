# U4P3 — Aplicación web con persistencia

## Objetivo

Diseñar un pequeño dominio de datos y desarrollar una aplicación web Spring Boot que permita consultar y registrar información persistente en MariaDB.

Puedes elegir el tema y el modelo, o utilizar el ejemplo incluido en este enunciado. La aplicación debe permitir consultar todos los registros y añadir uno mediante un formulario. Al reiniciarla, los datos registrados deben seguir disponibles.

**Duración orientativa:** 4–6 sesiones de 50 minutos.

## 1. Crear el proyecto

En Spring Initializr, crea un proyecto **Maven**, con **Java 23** y la versión de Spring Boot utilizada en el proyecto de referencia P3. Añade estas dependencias:

- Spring Web
- Thymeleaf
- Spring Data JPA
- Validation
- MariaDB Driver
- Lombok

No es necesario añadir dependencias de pruebas para completar esta práctica.

## 2. Elige un dominio

Propón una aplicación sencilla relacionada con un tema que te interese: actividades, libros, recetas, mascotas, viajes, música, deportes u otro. Define una entidad principal que tenga sentido en ese contexto.

Si no tienes una idea propia, puedes utilizar el ejemplo de **actividades de un centro cultural municipal de Sevilla** descrito más abajo.

## 3. Requisitos mínimos del modelo

La entidad principal debe tener un identificador numérico generado automáticamente y, como mínimo, **seis atributos propios**, sin contar el identificador. El modelo debe incluir:

- Al menos dos atributos de texto, uno obligatorio y otro opcional.
- Al menos un atributo numérico entero.
- Al menos un atributo numérico decimal.
- Al menos una fecha.
- Al menos un valor verdadero/falso.
- Reglas de validación adecuadas al significado de los datos: campos obligatorios, longitudes y límites numéricos, entre otras.

Elige un nombre y un propósito claros para cada atributo. Los datos no deben ser una colección de campos sin relación con el dominio elegido.
Antes de programar, prepara una tabla breve con los atributos, sus tipos, cuáles son obligatorios y las reglas aplicables. Esta tabla formará parte de la documentación de entrega.

En esta práctica se trabaja con **una entidad principal sin relaciones ORM**. No añadas otras entidades o relaciones para aumentar artificialmente el alcance.

## Ejemplo opcional: actividades de un centro cultural sevillano

La aplicación puede gestionar la programación de un centro cultural municipal de Sevilla, con actividades dirigidas a vecinos de distintos barrios: talleres en Triana, exposiciones en el casco histórico o actividades familiares en otros espacios de la ciudad. Cada actividad podría guardar:

| Atributo | Tipo orientativo | Reglas de ejemplo |
|---|---|---|
| `titulo` | Texto | Obligatorio; hasta 80 caracteres |
| `descripcion` | Texto | Opcional; hasta 300 caracteres |
| `fecha` | Fecha | Obligatoria |
| `duracionMinutos` | Entero | Entre 15 y 480 |
| `precio` | Decimal | Entre 0 y 999,99 |
| `aforo` | Entero | Entre 1 y 200 |
| `inscripcionAbierta` | Verdadero/falso | Obligatorio |

El ejemplo es una referencia para quien prefiera no inventar un dominio. Puedes cambiar sus nombres o reglas si mantienes los requisitos mínimos.

## 4. Base de datos

Cada estudiante trabajará con su propia instalación local de MariaDB. Elige un nombre para tu base de datos y créala vacía antes de iniciar la aplicación. La aplicación debe conectarse a esa base y Hibernate debe generar o actualizar la tabla a partir del modelo.

Configura Hibernate con `spring.jpa.hibernate.ddl-auto=update`. No uses `create` ni `create-drop`: podrían borrar los datos al reiniciar la aplicación. Configura las credenciales mediante las opciones indicadas en el proyecto de referencia; no guardes contraseñas reales en el código ni en Git.

## 5. Funcionalidades y organización

Implementa las siguientes funcionalidades:

1. Al abrir la página principal, se muestra el listado completo de registros.
2. En la misma página se muestra un formulario para añadir un registro.
3. Al enviar el formulario correctamente, el registro se guarda en MariaDB y se vuelve al listado actualizado.
4. Si el formulario contiene datos no válidos, se muestran los errores y se conservan los valores introducidos cuando corresponda.
5. Después de registrar uno o más elementos, reinicia la aplicación y comprueba que continúan en el listado.

Organiza el código siguiendo el recorrido de la aplicación de referencia P3: entidad, repositorio, servicio, controlador, objeto de entrada del formulario y vista. El controlador no debe acceder directamente al repositorio. Usa inyección de dependencias por constructor y mantén la lógica de acceso a datos fuera de la vista.

Utiliza MariaDB y las herramientas ya trabajadas en clase. Consulta P3 como referencia de estructura y flujo, pero adapta el dominio, el modelo, los nombres y la presentación a tu propuesta. Documenta brevemente en el proyecto qué responsabilidad tiene cada capa, por qué se separan la presentación y la lógica de negocio, qué tecnologías intervienen en el acceso a datos y dónde observas algún principio o patrón de diseño aplicado.

Añade comentarios breves en el código cuando ayuden a explicar una decisión de diseño o una parte que no resulte evidente. No comentes línea por línea ni repitas literalmente lo que ya expresan los nombres y anotaciones.

Antes de entregar, ejecuta la aplicación y comprueba que puedes cargar el listado y registrar datos válidos y no válidos. No se exige una suite automatizada de pruebas.

## 6. Ampliación opcional: consultas personalizadas

Amplía el listado con consultas propias que permitan acceder a subconjuntos útiles de los datos. Para el ejemplo del centro cultural, implementa estas dos opciones:

1. **Próximas actividades con inscripción abierta:** actividades cuya fecha no haya pasado y que tengan la inscripción abierta, ordenadas por fecha.
2. **Actividades gratuitas:** actividades con precio igual a cero, ordenadas por fecha.

Define las consultas en el repositorio mediante consultas personalizadas de Spring Data JPA. Añade en el servicio un método para cada opción y en la vista botones o enlaces que permitan acceder a cada listado. Cada opción debe mostrar un título claro y los resultados correspondientes; incluye también una forma de volver al listado completo.

Si has elegido un dominio propio, plantea dos consultas equivalentes que respondan a necesidades reales de ese dominio. Documenta qué criterios aplica cada consulta y cómo se accede a ella desde la vista.

## 7. Criterios de evaluación

En esta práctica se recogen evidencias de los siguientes criterios:

- **RA5 a)** Separar lógica de negocio y presentación: explicar en la documentación por qué se separan y qué ventajas tiene.
- **RA5 b)** Utilizar mecanismos y frameworks para esa separación: organizar la aplicación por capas y explicar brevemente el papel de Spring MVC, el servicio y Thymeleaf.
- **RA5 c)** Generar la presentación en el servidor: mostrar el listado y el formulario mediante Thymeleaf.
- **RA5 d)** Utilizar formularios para responder a eventos: procesar el envío del formulario de alta y mostrar los errores de validación.
- **RA5 e)** Aplicar parámetros de configuración: configurar la conexión con MariaDB y Hibernate.
- **RA5 g)** Aplicar principios y patrones de diseño orientado a objetos: identificar en la documentación un principio o patrón utilizado y dónde se observa.
- **RA5 h)** Probar y documentar el código: añadir comentarios breves y pertinentes que expliquen decisiones de diseño o partes no evidentes.
- **RA6 a)** Analizar tecnologías de acceso a datos: identificar en la documentación las tecnologías utilizadas y su papel.
- **RA6 b)** Establecer conexiones con bases de datos: conectar la aplicación con MariaDB.
- **RA6 c)** Recuperar información almacenada: cargar los registros mediante el repositorio y el servicio.
- **RA6 d)** Publicar información recuperada: mostrar los registros en la página web.
- **RA6 e)** Utilizar conjuntos de datos para almacenar información: guardar los registros del dominio en MariaDB.
- **RA6 g)** Probar y documentar aplicaciones web: añadir comentarios breves y pertinentes en el código.

**No se trabajan en esta práctica** RA5 f) —mantenimiento del estado de la aplicación web— ni RA6 f) —actualización y eliminación de información—.

## 8. Entrega

Entrega el proyecto Spring Boot y una breve documentación que indique:

- El dominio elegido y qué información representa cada atributo.
- El nombre de la base de datos local y cómo configurar la conexión.
- Cómo iniciar la aplicación y acceder a la página.
- Las responsabilidades de las capas, la razón de separar presentación y lógica, y las tecnologías utilizadas para acceder a los datos.
- Un principio o patrón de diseño que hayas aplicado y dónde se observa.
- Si realizas la ampliación, qué consultas personalizadas has añadido y cómo se accede a ellas.

## No se pide

- Editar ni eliminar registros.
- Inicio de sesión, gestión de usuarios o permisos.
- Relaciones entre entidades.
- Paginación ni consultas adicionales a las indicadas en la ampliación opcional.
- Publicar la aplicación en Internet.
