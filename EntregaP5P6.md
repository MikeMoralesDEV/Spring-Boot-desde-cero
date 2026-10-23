# Entrega P5–P6 — Agenda de actividades de un centro cultural

## Enunciado

Desarrolla una pequeña web para consultar las actividades de un centro cultural. La página de inicio presentará una selección de actividades y otra página explicará cómo organizar una visita. Las dos páginas compartirán elementos visuales mediante fragmentos Thymeleaf y ofrecerán sus textos en español e inglés.

Esta práctica reúne lo aprendido sobre **fragmentación y reutilización de plantillas** y **i18n**. No consiste en copiar los ejemplos P5 o P6: tendrás que proponer el contenido, las traducciones, la organización de las vistas y los fragmentos adecuados para este contexto.

La aplicación genera el HTML en el servidor con **Spring Boot, Spring MVC y Thymeleaf**. Los datos son fijos y didácticos; no se utiliza persistencia.

## Requisitos funcionales

1. La aplicación tendrá una página de inicio accesible en `/` y una página de visita accesible en `/visita`.
2. El controlador proporcionará un array o una colección de actividades en memoria. La página de inicio presentará al menos tres actividades culturales, cada una con título, una breve descripción y algún dato práctico, como fecha, duración o lugar.
3. La página `/visita` ofrecerá información propia del centro para ayudar a planificar la asistencia. No debe ser una copia de la página de inicio.
4. Ambas páginas compartirán como mínimo una cabecera, una navegación y un pie mediante fragmentos Thymeleaf. La cabecera recibirá contenido variable para reflejar la página actual.
5. Thymeleaf recorrerá las actividades con `th:each` y mostrará cada una mediante un fragmento parametrizado, pasando sus distintos títulos, descripciones y datos prácticos sin duplicar la estructura HTML.
6. El alumnado decidirá si integra cada fragmento con `th:replace` o con `th:insert`, y podrá explicar qué ocurre con el elemento anfitrión en cada caso.
7. La interfaz estará disponible en español e inglés. La selección de idioma se hará mediante una opción visible en la página y se procesará con `lang=es` o `lang=en`.
8. Los textos de interfaz estarán en archivos de mensajes localizados; no se duplicarán las plantillas por idioma ni se implementarán bloques `if` en Java para traducir frases.
9. Cada catálogo tendrá las mismas claves y una traducción para cada texto visible. Los textos que dependan de la página y el contenido práctico de las actividades también deben poder leerse en ambos idiomas.
10. El idioma elegido se conservará durante la sesión actual. No se guardarán actividades, usuarios ni otros datos de negocio en una base de datos.
11. Se utilizará `th:text` para insertar texto en la página y `@{...}` para construir los enlaces internos y las rutas de recursos estáticos.
12. La página debe incluir estilos propios y adaptarse a pantallas pequeñas.

## Requisitos técnicos y límites

- Crear la aplicación dentro de `p7springboot`.
- Mantener el estilo de los proyectos didácticos anteriores: controlador MVC, plantillas Thymeleaf, recursos estáticos y datos sencillos en memoria.
- Usar como mínimo dos plantillas de página y una plantilla de fragmentos.
- Usar archivos `messages.properties` y `messages_en.properties` para los mensajes.
- Configurar de forma explícita el idioma inicial y el mecanismo que permite cambiarlo.
- No añadir base de datos, persistencia, autenticación, DTO, entidades ni repositorios.
- No reutilizar literalmente el contenido, las frases ni las páginas de los ejemplos P5 o P6.
- Comentar el código de forma que otra persona que empieza pueda entender la función de los fragmentos y la selección de idioma.

## Ideas para organizar el trabajo

- Diseña dos páginas con propósitos distintos: descubrir actividades y preparar una visita.
- Decide qué estructura se repite realmente entre páginas y conviértela en fragmento.
- Separa las frases traducibles en claves coherentes, por ejemplo, agrupadas por página o componente.
- Pasa a los fragmentos los valores que cambian; evita duplicar la misma estructura para cada actividad.
- Revisa todos los enlaces para que la navegación y el cambio de idioma sigan funcionando desde ambas páginas.

Estas ideas orientan el diseño, pero no prescriben la solución exacta. Puedes organizar las plantillas y los nombres de claves de otra manera si cumples los requisitos y mantienes el código claro.

## Criterios de evaluación

La aplicación genera las páginas en el servidor: el controlador prepara los datos y Thymeleaf ejecuta las expresiones y genera el HTML (SSR). No se pide programar en JavaScript del lado cliente. La evaluación será **criterial**: cada criterio se revisará de manera independiente. La tabla identifica únicamente los criterios que se trabajan y pueden observarse en esta práctica. No se asigna puntuación ni nota a los apartados; la valoración se concretará posteriormente.

| Resultado de aprendizaje | Criterio | Evidencia de evaluación en esta práctica |
|---|---|---|
| RA5 | **a)** Se han identificado las ventajas de separar la lógica de negocio de los aspectos de presentación de la aplicación. | La estructura del proyecto separa el controlador y los datos de actividades de las plantillas Thymeleaf, los fragmentos y los catálogos de traducción. Se comprobará que el alumnado pueda señalar esta separación en su solución. |
| RA5 | **b)** Se han analizado y utilizado mecanismos y frameworks que permiten realizar esta separación y sus características principales. | Uso de Spring MVC para atender las rutas y preparar el modelo, y de Thymeleaf para generar las vistas, reutilizar fragmentos y resolver mensajes según el idioma. |
| RA5 | **c)** Se han utilizado objetos y controles en el servidor para generar el aspecto visual de la aplicación web en el cliente. | El controlador entrega datos al modelo y Thymeleaf genera en el servidor el HTML de las actividades, las páginas y los fragmentos. El selector de idioma solicita al servidor la versión correspondiente. |
| RA5 | **e)** Se han identificado y aplicado los parámetros relativos a la configuración de la aplicación web. | Configuración del idioma inicial, los catálogos de mensajes y el mecanismo de cambio de idioma; comprobación de que el idioma elegido se mantiene durante la sesión. |
| RA3 | **b)** Se han utilizado bucles y se ha verificado su funcionamiento. | Uso de `th:each` para recorrer en el servidor la colección de actividades y representación de todas sus entradas en la página generada. |
| RA3 | **c)** Se han utilizado «arrays» para almacenar y recuperar conjuntos de datos. | Array o colección de actividades preparado en el controlador y recuperado desde el modelo por la plantilla Thymeleaf durante el recorrido. |
| RA3 | **g)** Se han añadido comentarios al código. | Comentarios pertinentes en el controlador, la configuración, las plantillas o los fragmentos que aclaren las decisiones de i18n y reutilización, sin limitarse a repetir lo que ya expresa el código. |

## Criterios de aceptación

- `/` y `/visita` responden y muestran contenido diferente.
- El selector permite recorrer ambas páginas en español e inglés.
- Los enlaces de navegación y los de idioma funcionan desde las dos vistas.
- Las páginas comparten los fragmentos previstos y el contenido variable se pasa como parámetros.
- Las frases visibles de ambas páginas están traducidas; no quedan textos de interfaz olvidados en un idioma.
- No se ha añadido base de datos ni se guarda información de negocio.
- El código permite localizar dónde se configura el idioma, dónde están sus traducciones y dónde se definen e invocan los fragmentos.

## Entrega

Entrega el proyecto completo `p7springboot`, incluyendo el código fuente, los archivos de mensajes, las plantillas, los recursos estáticos y las pruebas que hayas creado. Añade una breve explicación en comentarios o en el propio código sobre las decisiones de fragmentación e i18n.
