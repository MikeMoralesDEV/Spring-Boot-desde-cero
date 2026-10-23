# Entrega P3-P4 — Préstamo de material del aula maker

## Criterios de evaluación de esta práctica

La calificación se asignará **por criterio**: cada criterio evaluado en las tablas se calificará de forma independiente con una nota de **0 a 10**. La práctica trabaja la programación del lado del servidor con **Java** y la integración de los datos en las plantillas con **Thymeleaf**. No se pide JavaScript: corresponde a otro módulo. Por ese motivo, **RA2(a)**, referido a la selección de un lenguaje de programación de clientes web, no se evalúa en esta práctica.

### Resultado de aprendizaje 2

| Criterio de evaluación | Evidencia que se evaluará en esta práctica |
|---|---|
| **b)** Se han utilizado los distintos tipos de variables y operadores disponibles en el lenguaje. | Usa tipos adecuados para textos, cantidades, fechas y valores booleanos; aplica operadores aritméticos, de comparación y lógicos en cálculos y validaciones. |
| **c)** Se han identificado los ámbitos de utilización de las variables. | Distingue en el código los atributos de instancia del DTO, los parámetros de métodos y las variables locales; los datos del modelo quedan disponibles en la plantilla que los recibe. |
| **d)** Se han reconocido y comprobado las peculiaridades del lenguaje respecto a las conversiones entre distintos tipos de datos. | Comprueba el binding desde los valores de texto del formulario a tipos Java como `Integer` y `LocalDate`, incluidos los errores de conversión. |
| **e)** Se han utilizado mecanismos de decisión en la creación de bloques de sentencias. | Usa decisiones en Java para resolver materiales y validar solicitudes, y una condición Thymeleaf para presentar el resultado según la duración. |
| **f)** Se han utilizado bucles y se ha verificado su funcionamiento. | Recorre el array de materiales con `th:each` y comprueba que aparecen todos sus elementos. |
| **g)** Se han añadido comentarios al código. | Incluye comentarios breves y útiles en Java y/o Thymeleaf para aclarar decisiones o partes relevantes. |
| **h)** Se han utilizado herramientas y entornos para facilitar la programación, prueba y documentación del código. | Desarrolla y ejecuta el proyecto con un IDE o Spring Initializr, comprueba los casos de aceptación y documenta cómo ejecutarlo en el `README.md`. |

### Resultado de aprendizaje 3

| Criterio de evaluación | Evidencia que se evaluará en esta práctica |
|---|---|
| **a)** Se han utilizado mecanismos de decisión en la creación de bloques de sentencias. | El controlador distingue, como mínimo, entre material existente e inexistente y entre solicitudes válidas y solicitudes con errores. |
| **b)** Se han utilizado bucles y se ha verificado su funcionamiento. | Usa `th:each` para recorrer y presentar el array de materiales; comprueba que aparecen todos. |
| **c)** Se han utilizado «arrays» para almacenar y recuperar conjuntos de datos. | Declara un array Java de materiales, lo pasa al modelo y recupera sus elementos al recorrerlo en Thymeleaf y al buscar un material por código. |
| **d)** Se han creado y utilizado funciones. | Crea y utiliza, como mínimo, un método auxiliar Java para buscar un material en el array y otro para calcular los días del préstamo. |
| **e)** Se han utilizado formularios Web para interactuar con el usuario del navegador Web. | Implementa el formulario web de solicitud de préstamo y su envío mediante `POST`. |
| **f)** Se han empleado métodos para recuperar la información introducida en el formulario. | Enlaza los campos enviados con el DTO mediante Spring MVC y procesa sus valores en el controlador. |
| **g)** Se han añadido comentarios al código. | Incluye comentarios breves y útiles en el controlador, el DTO o las plantillas cuando aclaren decisiones o comportamiento. |

Los arrays y la lógica se trabajan en Java y sus datos se presentan o recorren desde Thymeleaf. Las expresiones y atributos de Thymeleaf no son JavaScript.

## Enunciado

Desarrolla una aplicación web para consultar el material disponible en un aula maker y enviar solicitudes de préstamo. Una persona podrá abrir la ficha de un recurso desde un catálogo, completar un formulario y ver un resumen de su solicitud.

La aplicación debe generar las páginas en el servidor con **Spring Boot, Spring MVC y Thymeleaf**. La entrega combina navegación y manejo de errores (P3) con formularios, enlace de datos y validación (P4).

El catálogo y las solicitudes serán temporales: **no se utilizarán base de datos, servicios ni persistencia**. Una solicitud válida solo se mostrará en la página de resultado; no se guardará.

## Objetivos de evaluación

- Construir enlaces internos con Thymeleaf y distinguir parámetros de consulta de variables de ruta.
- Recibir datos de una petición en Spring MVC y pasarlos a una plantilla mediante el `Model`.
- Mostrar páginas de error personalizadas con los estados HTTP adecuados.
- Enviar un formulario mediante `POST`, enlazarlo a un DTO y validar los datos en el servidor.
- Volver a mostrar el formulario con sus valores y mensajes cuando los datos no sean válidos.
- Mostrar el resultado sin insertar los datos recibidos como HTML.

## Requisitos funcionales

### 1. Catálogo y navegación

1. La ruta `GET /` mostrará un catálogo de recursos. Declara en Java un array bidimensional `String[][]` en memoria; cada fila representará un material y contendrá, en este orden, su código, nombre y descripción. El controlador proporcionará el array a la plantilla y Thymeleaf lo recorrerá con `th:each`. Incluye, como mínimo:
   - `PORTATIL` — Ordenador portátil; incluye un cargador y una funda.
   - `MICRO` — Kit de microcontrolador; incluye una placa y un cable USB.
   - `CAMARA` — Cámara de documentación; incluye un soporte de mesa.
2. Cada recurso tendrá un enlace a su ficha. Genera el enlace en Thymeleaf con `th:href`; no escribas las URL internas como rutas fijas en el HTML.
3. La ficha estará disponible en `GET /materiales/{codigo}`. Recibirá `codigo` como variable de ruta con `@PathVariable` y mostrará el nombre, el código y una descripción del recurso.
4. La ficha aceptará además el parámetro de consulta opcional `turno`. Si no se proporciona, mostrará `general`. Por ejemplo, `/materiales/MICRO?turno=tarde`. Recíbelo con `@RequestParam` y muestra el turno solicitado.
5. La ficha incluirá un enlace a `GET /solicitud/nueva`, desde donde se inicia el formulario. Las páginas permitirán volver al catálogo.
6. Si se solicita un código que no existe, responde con estado HTTP **404** y muestra la página de error correspondiente. No muestres una ficha inventada ni devuelvas un resultado normal con un mensaje de error.
7. Crea y utiliza un método auxiliar Java que busque un material por su código en el array. Usa una decisión para distinguir si lo encuentra y devuelve un 404 cuando no exista.

### 2. Formulario de préstamo

La ruta `GET /solicitud/nueva` mostrará un formulario con estos campos:

| Campo | Tipo sugerido | Regla de validación del servidor |
|---|---|---|
| Nombre de quien solicita | Texto | Obligatorio; máximo 60 caracteres. |
| Correo electrónico | Correo | Obligatorio, formato de correo válido; máximo 120 caracteres. |
| Material solicitado | Selector | Obligatorio; solo se aceptan los códigos incluidos en el catálogo. |
| Unidades | Número entero | Obligatorio; entre 1 y 3, ambos incluidos. |
| Fecha de recogida | `LocalDate` | Obligatoria; hoy o una fecha futura. |
| Fecha de devolución | `LocalDate` | Obligatoria; hoy o una fecha futura, y no anterior a la recogida. |
| Motivo del préstamo | Área de texto | Obligatorio; entre 30 y 400 caracteres. |
| Aceptación de las normas | Casilla | Debe estar marcada para enviar la solicitud. |

8. El formulario se enviará con `POST` a `/solicitudes`. Agrupa sus campos en un DTO propio, por ejemplo `SolicitudPrestamoForm`; no recibas todos los campos como parámetros sueltos.
9. Enlaza el formulario al DTO con `th:object` y `th:field`. Usa anotaciones de Jakarta Bean Validation para las reglas que correspondan y `@Valid` en el controlador.
10. La regla «la fecha de devolución no puede ser anterior a la fecha de recogida» compara dos campos. Valídala en el controlador después del binding y añade el error al campo correspondiente mediante `BindingResult`.
11. Coloca `BindingResult` inmediatamente después del DTO validado en la firma del controlador. Si hay errores, vuelve a mostrar el formulario, conserva los valores que se hayan podido enlazar y presenta cada mensaje junto al campo afectado.
12. Vuelve a incluir en el modelo las opciones del selector cuando se vuelva a renderizar el formulario con errores.
13. Si la solicitud es válida, muestra una página de resultado con un resumen de todos los datos recibidos. No guardes la solicitud ni la presentes como si se hubiera reservado o aprobado el material.
14. Crea y utiliza un método auxiliar Java para calcular y mostrar el número de días del préstamo (contando los días de recogida y devolución) y el total de días-unidad (días del préstamo multiplicados por unidades). Usa tipos numéricos adecuados.
15. En la plantilla de resultado, utiliza una condición Thymeleaf para distinguir entre un préstamo de un día y uno de varios días, y presenta un mensaje adecuado en cada caso.

En el código, utiliza y permite identificar parámetros de método, variables locales y propiedades del DTO. En el `README.md`, describe brevemente el ámbito de cada uno y cómo los atributos del modelo se utilizan en la vista.

Para las fechas, usa campos `LocalDate` y enlázalos con el formato ISO de los controles HTML de fecha. Aplica una validación de fecha presente o futura en el servidor; no basta con configurar el atributo `min` del navegador.

El binding convierte valores enviados como texto a los tipos Java del DTO. Comprueba también qué ocurre si se envía una cantidad que no sea un número entero o una fecha con un formato inválido: el formulario debe volver a mostrarse con un mensaje comprensible, sin presentar un resultado válido.

### 3. Errores y presentación

16. Crea plantillas de error para `error/404.html`, `error/4xx.html` y `error/5xx.html`. Deben explicar de forma comprensible que ha ocurrido un error y ofrecer un enlace para volver al catálogo. No expongas trazas ni detalles internos.
17. El error de un código de material inexistente debe producir una respuesta HTTP 404 real. Spring Boot debe poder elegir la plantilla específica `404.html` frente a la plantilla general `4xx.html`.
18. Usa `th:text` para mostrar en las páginas los valores introducidos o recibidos en la petición. No uses `th:utext` para esos datos.
19. Da formato a las páginas para que el catálogo, el formulario, los errores y el resumen sean legibles en móvil y escritorio. La presentación no sustituye a la validación del servidor.

## Requisitos técnicos y límites

- Crea un proyecto Spring Boot independiente llamado `entregaP3P4`, al mismo nivel que `p3springboot` y `p4springboot`. No modifiques los proyectos didácticos.
- Incluye Spring Web, Thymeleaf, Validation y las dependencias necesarias para ejecutar la aplicación.
- Mantén el catálogo en memoria como un array Java `String[][]`, según la estructura indicada. No hace falta crear clases de dominio para los materiales.
- El DTO es un objeto de entrada del formulario, no una entidad de base de datos.
- Separa las plantillas de Thymeleaf de los recursos estáticos y utiliza CSS propio si lo necesitas.
- Añade comentarios breves que aclaren las decisiones relevantes del controlador, del DTO y de las plantillas; evita comentar instrucciones obvias.
- Usa Java para la lógica del servidor y Thymeleaf para presentar y recorrer los datos; no es necesario ni se pide JavaScript.
- Los atributos HTML como `required`, `min`, `max` y `maxlength` pueden ayudar en el navegador, pero no reemplazan las validaciones de Java.
- No añadas autenticación, sesiones, base de datos, repositorios, envío de correo ni operaciones reales de préstamo.

## Criterios de aceptación

Comprueba manualmente, como mínimo, estos casos:

| Prueba | Resultado esperado |
|---|---|
| Abrir `/` | Aparece el catálogo y hay enlaces funcionales a las fichas. |
| Abrir `/materiales/MICRO?turno=tarde` | La ficha muestra el kit de microcontrolador y el turno «tarde». |
| Abrir `/materiales/MICRO` | La ficha muestra el turno predeterminado «general». |
| Abrir `/materiales/NO-EXISTE` | La respuesta tiene estado 404 y se muestra la página específica de ese error. |
| Enviar el formulario vacío | Se vuelve al formulario con errores; las opciones del selector siguen disponibles. |
| Enviar un correo no válido, cero unidades o un motivo demasiado corto | Se muestran errores de validación del servidor y se conservan los demás valores. |
| Enviar texto en el campo de unidades o una fecha con formato inválido | Se muestra un error de conversión comprensible y no se presenta un resultado válido. |
| Enviar una devolución anterior a la recogida | Se muestra un error en la fecha de devolución y no se presenta un resultado válido. |
| Enviar todos los campos correctamente | Se muestra el resumen, los días del préstamo, el total de días-unidad y el mensaje condicional apropiado; la solicitud no queda guardada. |
| Revisar el catálogo | Se muestran todos los materiales del array, cada uno con un enlace a su ficha. |
| Introducir texto con caracteres HTML en un campo | Se muestra como texto escapado, no como contenido HTML ejecutable. |

Además, verifica que existen las plantillas generales `4xx.html` y `5xx.html`, y que todos los formularios y enlaces internos funcionan sin depender de rutas absolutas escritas a mano.

## Entrega

Entrega el proyecto completo y ejecutable, incluyendo:

- Código Java del controlador y del DTO.
- Plantillas Thymeleaf para catálogo, ficha, formulario, resultado y errores.
- Recursos estáticos utilizados.
- Un `README.md` breve con los requisitos para ejecutar el proyecto, las rutas principales y las comprobaciones realizadas.

## Calificación por criterio

Se asignará **una nota independiente de 0 a 10 a cada uno de los 14 criterios evaluados**. No se suman ni se promedian estas notas para formar una única calificación global de esta rúbrica. **RA2(a) queda fuera** porque la práctica no desarrolla programación de cliente web.

| Resultado de aprendizaje | Criterio | Evidencia para asignar la nota | Nota (0-10) |
|---|---|---|---:|
| RA2 | b) Tipos de variables y operadores | Utiliza tipos adecuados para datos y cálculos; aplica operadores aritméticos, de comparación y lógicos correctamente. | /10 |
| RA2 | c) Ámbitos de variables | Identifica y usa correctamente parámetros, variables locales, propiedades del DTO y atributos disponibles en la vista. | /10 |
| RA2 | d) Conversiones entre tipos | El binding y sus errores de conversión a tipos como `Integer` y `LocalDate` se comprueban y gestionan correctamente. | /10 |
| RA2 | e) Mecanismos de decisión | Las decisiones Java de búsqueda y validación, así como la condición Thymeleaf del resultado, funcionan correctamente. | /10 |
| RA2 | f) Bucles | El recorrido Thymeleaf del array presenta todos los materiales sin omisiones ni errores. | /10 |
| RA2 | g) Comentarios | Los comentarios son correctos, útiles y aclaran decisiones relevantes sin limitarse a repetir el código. | /10 |
| RA2 | h) Herramientas, pruebas y documentación | El proyecto se ejecuta; se comprueban los casos de aceptación y el `README.md` explica cómo ejecutarlo y las comprobaciones realizadas. | /10 |
| RA3 | a) Mecanismos de decisión | Distingue correctamente los casos de material encontrado/no encontrado y solicitud válida/no válida. | /10 |
| RA3 | b) Bucles y verificación | Recorre el array con Thymeleaf y comprueba que se muestra cada material. | /10 |
| RA3 | c) Arrays para almacenar y recuperar datos | Declara el array, lo proporciona a la vista, lo recorre y recupera materiales por código correctamente. | /10 |
| RA3 | d) Creación y uso de funciones | Crea y utiliza métodos auxiliares Java, como la búsqueda por código y el cálculo de días. | /10 |
| RA3 | e) Formularios web | El formulario permite introducir y enviar la solicitud mediante `POST`. | /10 |
| RA3 | f) Recuperación de los datos del formulario | El DTO recibe los campos enviados y el controlador procesa correctamente sus valores. | /10 |
| RA3 | g) Comentarios | Los comentarios en Java o Thymeleaf aclaran el comportamiento relevante y son correctos. | /10 |

La entrega debe compilar y arrancar para poder evaluarse. Un control del navegador sin validación equivalente en el servidor no cuenta como validación completada.
