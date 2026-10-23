# Entrega P2 — Mi plan de estudio

## Enunciado

Desarrolla una página web que muestre el plan de estudio de una persona para el día. La página saludará al estudiante, presentará las asignaturas que tiene previstas y permitirá consultar de un vistazo cuánto tiempo dedicará a estudiar.

La aplicación debe generar el HTML en el servidor usando **Spring Boot, Spring MVC y Thymeleaf**. El objetivo de la entrega es practicar cómo se insertan valores en una plantilla, cómo se recorren listas y cómo se usan las utilidades de Thymeleaf.

Esta entrega está relacionada con los criterios de evaluación **a, b, c y e** de la unidad didáctica.

## Requisitos funcionales

1. La aplicación tendrá una única página accesible mediante una petición `GET` a `/`.
2. Se podrá personalizar el saludo mediante el parámetro de consulta `nombre`. Por ejemplo, `/?nombre=Lucía` mostrará un saludo dirigido a Lucía. Si no se proporciona, se utilizará un nombre predeterminado.
3. La página mostrará el nombre del estudiante y un título para su plan de estudio.
4. La aplicación presentará una lista de asignaturas. Cada elemento será un texto sencillo; no se crearán clases propias para representar asignaturas.
5. Thymeleaf recorrerá la lista con `th:each` y mostrará las asignaturas numeradas utilizando el índice del estado de iteración.
6. Se indicará cuántas asignaturas contiene la lista mediante la utilidad `#lists`.
7. Se mostrará junto a cada asignatura su duración en minutos. Las asignaturas serán cadenas y las duraciones números sencillos.
8. Se utilizará `#numbers` en la plantilla al menos de una de estas maneras:
   - `#numbers.sequence(...)` para generar una secuencia numérica, por ejemplo, los números de una lista.
   - `#numbers.formatDecimal(...)` para presentar un valor decimal con un formato legible.
9. La plantilla utilizará `th:text` para insertar valores de forma segura.

## Requisitos técnicos y límites

- Crear la aplicación dentro del proyecto `p2springboot`.
- Usar **una sola plantilla Thymeleaf** para la página.
- Mantener los datos en el controlador como valores simples y listas de cadenas o números.
- Crear el proyecto con [Spring Initializr](https://start.spring.io/) o directamente desde el IDE.
- No crear DTO, clases de dominio, servicios ni repositorios.
- No añadir formularios, base de datos ni persistencia.
- Usar comentarios breves para explicar las expresiones de Thymeleaf menos evidentes.
- Dar formato a la página para que sea clara y cómoda de leer en escritorio y móvil.

## Datos de partida sugeridos

El controlador puede proporcionar, por ejemplo:

- Nombre predeterminado: `estudiante`.
- Asignaturas: `Programación`, `Bases de datos`, `Lenguajes de marcas`.
- Duraciones: `25`, `40`, `30` minutos.

Mantén las dos listas con el mismo número de elementos y el mismo orden: la duración de la posición cero corresponde a la asignatura de la posición cero, y así sucesivamente.

### Pista: relacionar asignaturas y duraciones

No necesitas dos recorridos independientes. Recorre la lista de asignaturas con un solo `th:each` y captura su estado de iteración. El estado incluye el índice de la asignatura actual; puedes usar ese índice para consultar la duración que ocupa la misma posición en la otra lista. Por ejemplo, con `iterStat` como nombre del estado, la idea es `${duraciones[iterStat.index]}`.

## Criterios de aceptación

- Al abrir `/`, se renderiza la plantilla sin errores.
- Al abrir `/?nombre=Lucía`, el saludo muestra «Lucía».
- Las asignaturas aparecen numeradas por índice y cada una muestra su duración correspondiente.
- El contador refleja el número real de asignaturas.
- Se puede identificar en el código dónde se usan `#lists` y `#numbers` y explicar qué resuelve cada utilidad.
- El contenido recibido por URL se inserta con `th:text`, no como HTML sin escapar.

## Ampliación opcional

Usa `#numbers.sequence(...)` para mostrar una pequeña escala de números relacionada con el plan, por ejemplo, una numeración adicional o una escala de horas.

No uses condiciones (`th:if`, `th:unless` u otras): todavía no se han visto en clase.
