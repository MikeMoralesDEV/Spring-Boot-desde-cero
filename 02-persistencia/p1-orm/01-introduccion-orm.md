# Introducción al ORM

## Guía de estudio

Esta guía es para ti si ya conoces Java, las bases de datos relacionales, SQL y los fundamentos de Spring Boot trabajados en la fase anterior. Aquí aprenderás qué significa mapear objetos a tablas y qué papel tienen JPA, Hibernate y Spring Data JPA, antes de escribir una entidad o configurar un proyecto.

**Todavía no es un tutorial de programación ni un ejercicio de CRUD.** Primero necesitas entender las ideas y el vocabulario. Después podrás reconocerlos cuando aparezcan en una aplicación.

## 1. Objetivos de aprendizaje

Al terminar esta guía, deberías poder:

- Explicar qué problema aborda un ORM y qué trabajo sigue siendo responsabilidad de la persona que desarrolla la aplicación.
- Relacionar una clase y sus objetos con una tabla y sus filas sin afirmar que ambos modelos son idénticos.
- Distinguir una clase de dominio, una entidad persistente, un DTO y el `Model` de Spring MVC.
- Explicar la diferencia entre JPA, Hibernate y Spring Data JPA.
- Describir, a nivel general, qué significa que una entidad esté gestionada y cómo se sincronizan sus cambios con la base de datos.
- Identificar beneficios, límites y errores habituales al utilizar un ORM.
- Reconocer qué conceptos se dejan intencionadamente para sesiones posteriores.

## 2. Punto de partida: dos maneras de representar información

Ya conoces las tablas relacionales y los objetos Java. Antes de unirlos, repasemos qué representa cada uno por separado.

### En una base de datos relacional

- Una **tabla** organiza información de un tipo.
- Una **fila** representa un registro.
- Una **columna** almacena un valor con un tipo y unas restricciones.
- Una **clave primaria** permite identificar una fila.
- Las restricciones, claves foráneas y transacciones pertenecen al modelo y comportamiento relacional de la base de datos.

### En Java

- Una **clase** define una estructura y un comportamiento.
- Un **objeto** es una instancia concreta de esa clase.
- Sus **atributos** contienen valores.
- La identidad de un objeto en memoria no es automáticamente la misma que la identidad de una fila.
- Un objeto puede referenciar otro objeto y puede contener colecciones; una tabla no representa esas referencias exactamente de la misma manera.

### Pregunta para pensar

> Si la aplicación ya trabaja con objetos, pero la base de datos almacena filas, ¿quién convierte una representación en la otra y cuándo?

Piensa cómo resolverías esa conversión usando lo que ya sabes de JDBC: ejecutar SQL, leer resultados y construir o actualizar objetos. Un ORM ayuda a automatizar parte de ese trabajo.

## 3. El problema objeto-relacional

Las bases de datos relacionales y los lenguajes orientados a objetos organizan la información de forma distinta. La diferencia se suele llamar **desajuste objeto-relacional**.

Al guardar datos manualmente, el código debe coordinar varias tareas:

1. Preparar una operación de base de datos.
2. Enviar los valores con los tipos correctos.
3. Ejecutar la operación.
4. Interpretar las filas devueltas.
5. Construir objetos y asignarles los valores adecuados.
6. Resolver identificadores y referencias entre registros.
7. Gestionar errores y transacciones.

JDBC proporciona herramientas para realizar esas tareas. No es un mecanismo incorrecto ni desaparece al utilizar un ORM: es una forma más directa y explícita de comunicarse con una base de datos relacional.

Cuando una aplicación tiene muchas operaciones similares, la conversión repetida entre filas y objetos puede ocupar una parte significativa del código. Un ORM automatiza y coordina buena parte de esa conversión siguiendo un mapeo definido por la aplicación.

## 4. Qué es un ORM

**ORM** significa *Object-Relational Mapping*, o mapeo objeto-relacional. También se usa la sigla para referirse a las herramientas que implementan ese enfoque.

Un ORM mantiene una correspondencia entre conceptos del programa y estructuras relacionales. Por ejemplo:

| En el programa | En la base de datos |
|---|---|
| Clase persistente | Tabla |
| Objeto de esa clase | Fila o registro |
| Atributo persistente | Columna |
| Identificador del objeto | Clave primaria |
| Referencia entre objetos | Relación entre registros |

La tabla ayuda a explicar la idea, pero no significa que el mapeo sea siempre una traducción mecánica uno a uno. La estructura de objetos y el diseño relacional tienen reglas distintas; una aplicación debe decidir cómo relacionarlas.

### Qué puede hacer el ORM

Según la operación y el estado de los objetos, un ORM puede:

- Traducir cambios de objetos a operaciones de base de datos.
- Ejecutar consultas y convertir filas en objetos.
- Mantener una identidad coherente para los objetos cargados durante una unidad de trabajo.
- Detectar cambios en objetos que están bajo su seguimiento.
- Coordinar las operaciones con transacciones y generar SQL.
- Resolver parte del trabajo repetitivo de asignar columnas y atributos.

### Qué no hace por el equipo

Un ORM no:

- Diseña automáticamente un buen modelo de dominio.
- Decide qué datos necesita el negocio ni qué restricciones son correctas.
- Elimina la necesidad de conocer tablas, claves, SQL o transacciones.
- Garantiza que cualquier consulta sea rápida.
- Hace que todos los motores de base de datos se comporten de forma idéntica.
- Sustituye la validación de entrada, la autorización ni las reglas de negocio.
- Convierte una estructura relacional en un modelo de objetos correcto sin decisiones de diseño.

**Idea clave:** el ORM reduce trabajo mecánico, pero añade conceptos y comportamiento que hay que entender.

## 5. JDBC, ORM y JPA: no son sinónimos

Es importante comparar estos nombres porque suelen usarse como si significaran lo mismo.

### JDBC

JDBC es la API de Java para comunicarse con bases de datos relacionales mediante un driver. El código suele trabajar de forma explícita con conexiones, sentencias, parámetros y resultados.

JDBC responde a la pregunta:

> ¿Cómo puede un programa Java enviar operaciones a una base de datos y leer sus resultados?

### ORM

ORM es el enfoque de relacionar objetos y estructuras relacionales. Describe una forma de resolver el trabajo entre ambos modelos; no designa por sí solo una API concreta de Java.

### JPA

JPA, hoy conocida como Jakarta Persistence, es una **especificación**: define conceptos, interfaces y reglas comunes para persistir objetos Java. Una especificación describe cómo se espera que funcione una solución, pero necesita una implementación que haga el trabajo.

JPA responde a la pregunta:

> ¿Qué contrato común pueden implementar las herramientas de persistencia para Java?

### Hibernate

Hibernate ORM es una implementación muy conocida de Jakarta Persistence y, además, ofrece funcionalidades propias. En un proyecto configurado con JPA, Hibernate puede ser el proveedor que realiza el mapeo y la comunicación efectiva con la base de datos.

Hibernate responde a la pregunta:

> ¿Qué herramienta concreta ejecuta el mapeo y las operaciones definidas por la aplicación?

### Spring Data JPA

Spring Data JPA es una capa de Spring que facilita el acceso a repositorios sobre JPA. Reduce código repetitivo para operaciones habituales y ofrece formas de declarar consultas, pero no es el ORM ni reemplaza al proveedor JPA.

Responde a la pregunta:

> ¿Cómo puede Spring ofrecer una forma conveniente y coherente de trabajar con repositorios JPA?

### Resumen de responsabilidades

| Nombre | Qué es | Papel principal |
|---|---|---|
| JDBC | API de Java | Comunicación de bajo nivel con una base de datos mediante un driver |
| ORM | Enfoque y categoría de herramientas | Correspondencia entre objetos y estructuras relacionales |
| Jakarta Persistence (JPA) | Especificación | Contrato y conceptos estándar de persistencia de objetos |
| Hibernate ORM | Implementación de ORM y proveedor JPA | Ejecuta el mapeo, genera/ejecuta operaciones y gestiona entidades |
| Spring Data JPA | Proyecto de Spring | Simplifica el acceso a datos mediante repositorios sobre JPA |

Una aplicación Spring Boot puede usar Spring Data JPA, que opera sobre JPA, cuya implementación puede ser Hibernate, y que finalmente utiliza un driver para comunicarse con la base de datos. Es una cadena de responsabilidades, no cinco nombres para la misma pieza.

## 6. Qué es una entidad persistente

Una **entidad** es un objeto cuya identidad se conserva en el sistema de persistencia. Puede existir una entidad aunque algunos de sus atributos cambien: sigue representando el mismo registro mientras conserva su identidad persistente.

Por ejemplo, cambiar el nombre descriptivo de un recurso no debería convertirlo automáticamente en otro recurso distinto. La identidad permite localizar y reconocer el registro con independencia de sus demás valores.

Para que una clase pueda participar en el mapeo JPA como entidad, hay que definir, entre otras cosas:

- Qué clase representa una entidad.
- Cómo se identifica de forma única.
- Qué atributos se guardan y cómo se relacionan con el esquema.
- Qué restricciones o reglas de mapeo se aplican.

La declaración exacta y sus anotaciones se estudiarán más adelante, cuando creemos el primer modelo persistente. Por ahora basta con entender la idea.

### Identidad frente a igualdad

Dos objetos pueden tener los mismos valores en sus atributos y, sin embargo, representar registros diferentes. También pueden representar el mismo registro aunque ciertos valores hayan cambiado.

Por eso conviene preguntar:

- ¿Qué hace que dos objetos sean el mismo registro?
- ¿Qué atributo o mecanismo identifica un registro?
- ¿Una descripción igual implica que es el mismo objeto persistente?

La respuesta no debe basarse accidentalmente en que dos objetos “se ven iguales”. El diseño de identidad se tratará al modelar la primera entidad.

## 7. Cuatro significados diferentes de “modelo”

En las conversaciones de Spring y desarrollo web se usa la palabra **modelo** para varias cosas. Distinguirlas desde el principio evita errores de arquitectura y de vocabulario.

### Modelo del dominio

Representa conceptos relevantes del problema que se está resolviendo y sus reglas. Puede ser sencillo o complejo y no implica por definición que se guarde en una base de datos.

### Entidad persistente

Es una clase que el mecanismo de persistencia puede mapear a datos almacenados. En muchos proyectos representa también una parte del dominio; en otros se separa. La decisión depende del diseño y del alcance.

### DTO u objeto de formulario

Transporta datos entre una petición y una parte de la aplicación. En P4 se utilizó un objeto de formulario para recibir valores, enlazarlos y validarlos. Ese objeto no se convirtió por ello en una entidad ni se guardó automáticamente.

### `Model` de Spring MVC

Es el contenedor que el controlador usa para proporcionar atributos a la plantilla Thymeleaf durante una petición. No es el modelo del dominio, no es una entidad y no es una base de datos.

### Una misma información puede pasar por varios objetos

Una petición puede incluir datos de formulario; Spring los enlaza a un objeto de entrada; la aplicación valida esos datos; una operación de negocio decide qué hacer; la persistencia guarda o recupera entidades; finalmente, el controlador proporciona a la vista los datos que esta necesita.

Esta descripción no obliga a crear un objeto distinto en cada paso. Sirve para entender que **recibir datos, representar el problema, almacenar información y presentar la vista son responsabilidades diferentes**, aunque en aplicaciones pequeñas algunas clases puedan tener más de una responsabilidad.

## 8. El contexto de persistencia y los estados de una entidad

En JPA, un contexto de persistencia realiza un seguimiento de las entidades gestionadas durante una unidad de trabajo. En una aplicación Spring, suele estar asociado a una operación transaccional, aunque los detalles se estudiarán en el apartado de transacciones.

El seguimiento hace posible que el proveedor detecte cambios sin que el programa tenga que enviar manualmente una operación de actualización cada vez que cambia un atributo.

### Estados conceptuales

Una entidad puede encontrarse en diferentes estados:

1. **Nueva o transitoria:** existe como objeto en Java, pero aún no está asociada al contexto de persistencia ni tiene necesariamente una fila.
2. **Gestionada:** el contexto de persistencia la conoce y puede seguir sus cambios.
3. **Separada o detached:** tiene identidad persistente, pero ya no está bajo seguimiento en el contexto actual.
4. **Eliminada:** se ha marcado para eliminarse y la operación se sincronizará con la base de datos al realizar el flush/commit correspondiente.

Los nombres describen la relación del objeto con el contexto, no solo si la variable Java existe.

### Ciclo simplificado

Una secuencia frecuente, descrita sin código, es:

1. La aplicación solicita o prepara una entidad.
2. El proveedor la asocia a un contexto de persistencia.
3. La aplicación lee o modifica sus atributos.
4. El proveedor detecta cambios y prepara las operaciones necesarias.
5. Las operaciones se sincronizan con la base de datos.
6. La transacción termina y el contexto puede cerrarse.

No hay que interpretar esto como una garantía de que cada cambio de atributo produce inmediatamente una consulta SQL. El momento en que se envía SQL depende del contexto, el flush y la transacción.

## 9. Dirty checking, flush y commit

Estos tres conceptos están relacionados, pero no son iguales.

- **Dirty checking** es la detección de cambios en entidades gestionadas.
- **Flush** sincroniza con la base de datos los cambios pendientes del contexto de persistencia. Puede generar y ejecutar SQL, pero no significa por sí solo que la transacción esté confirmada de forma definitiva.
- **Commit** confirma la transacción de base de datos, si la operación termina correctamente.

Una forma intuitiva de recordarlo:

> El objeto gestionado cambia; el proveedor detecta el cambio; el flush sincroniza; el commit confirma la transacción.

La secuencia y el momento exacto dependen del uso de JPA y de la gestión transaccional. En la primera explicación basta con comprender que “cambiar un objeto” y “confirmar ese cambio en la base de datos” son momentos conceptualmente distintos.

## 10. Cómo se leen y escriben datos

### Escritura

La aplicación solicita que una entidad nueva se haga persistente o modifica una entidad gestionada. El proveedor utiliza el mapeo para preparar las operaciones correspondientes. La base de datos sigue siendo quien aplica sus reglas y restricciones.

### Lectura

La aplicación solicita registros mediante una búsqueda o consulta. El proveedor ejecuta la operación y convierte los resultados en entidades. Si la entidad ya está gestionada en el contexto actual, el proveedor debe mantener una identidad coherente dentro de ese contexto.

### Eliminación

La aplicación solicita eliminar una entidad o registro. La operación puede verse afectada por claves foráneas, relaciones y reglas de la base de datos. No significa que todo objeto referenciado deba borrarse automáticamente.

Las tres operaciones usan el mapeo, pero pueden generar SQL distinto según el motor, la configuración, el estado de la entidad y la consulta.

## 11. Las consultas siguen importando

Un ORM no elimina las consultas: las genera a partir del mapeo y de la petición de la aplicación. El equipo sigue teniendo que decidir:

- Qué información quiere recuperar.
- Qué condiciones debe cumplir.
- Qué columnas o entidades son necesarias.
- Si se precisa ordenar o limitar el resultado.
- Qué coste puede tener recuperar esa información.

Más adelante estudiarás repositorios y consultas de forma gradual. Tu conocimiento de SQL sigue siendo una herramienta fundamental para comprender qué trabajo hace el ORM y para investigar problemas.

Si una operación generada no se entiende, conviene inspeccionar el SQL, el esquema y el plan de ejecución antes de concluir que “Hibernate se equivoca”.

## 12. Ventajas y costes

### Ventajas posibles

- Reduce mapeo repetitivo entre filas y objetos.
- Centraliza parte de la definición del mapeo.
- Facilita expresar operaciones comunes sobre entidades.
- Puede ayudar a desacoplar parte del código de un motor concreto.
- Se integra con transacciones, validación y otras capacidades del ecosistema.
- Puede mejorar la legibilidad cuando se usa con un modelo y consultas bien diseñados.

### Costes y riesgos

- Hay conceptos nuevos: entidades gestionadas, contexto, carga, flush y transacción.
- Una operación sencilla en apariencia puede generar más consultas o consultas menos eficientes de lo esperado.
- Las relaciones entre objetos pueden ocultar cuántas consultas se ejecutan.
- Un mapeo equivocado puede producir un esquema o comportamiento que no representa el problema.
- El proveedor puede generar SQL distinto según el motor y la configuración.
- La abstracción puede hacer más difícil diagnosticar problemas si el equipo no observa el SQL.
- Para consultas muy específicas puede ser necesario utilizar JPQL, SQL nativo u otra técnica adecuada.

**Conclusión equilibrada:** ORM es una herramienta útil, no una obligación para todo acceso a datos ni una mejora automática sobre JDBC.

## 13. Portabilidad y dependencia del motor

JPA ayuda a separar parte del código de las particularidades de un proveedor o una base de datos, pero **portabilidad no significa identidad de comportamiento ni ausencia de SQL**.

Las bases de datos pueden diferir en:

- Tipos y rangos de valores.
- Funciones disponibles.
- Reglas de mayúsculas, intercalación y comparación.
- Tratamiento de fechas y zonas horarias.
- Sintaxis de paginación.
- Restricciones, índices y generación de identificadores.
- Rendimiento y planes de consulta.

El proveedor puede adaptar parte del SQL, normalmente mediante el conocimiento del dialecto adecuado. Aun así, una aplicación debe probarse con el motor que utilizará y evitar depender sin necesidad de particularidades no previstas.

Podrás elegir una base de datos local para los ejemplos, pero cada combinación debe configurarse y verificarse: no basta con cambiar el nombre del driver.

## 14. Términos que se posponen

Estos temas son importantes, pero se estudiarán más adelante, cuando tengas los conceptos básicos:

- **Relaciones entre entidades:** se abordarán después de mapear una entidad sencilla y comprender consultas iniciales.
- **Carga diferida y anticipada:** determinan cuándo se recuperan datos relacionados.
- **Problema N+1:** patrón de consultas repetidas que puede aparecer al recorrer relaciones.
- **Transacciones:** determinan cuándo se confirman o revierten operaciones.
- **Cascadas y eliminación de relaciones:** definen cómo se propagan ciertas operaciones.
- **Migraciones:** controlan la evolución explícita del esquema a lo largo del tiempo.
- **Caché:** puede afectar a las consultas y a la visibilidad de cambios.

Reconócelos como conceptos que aparecerán más adelante; no necesitas dominarlos todavía.

## 15. Errores de comprensión que interesa corregir

### “JPA es Hibernate”

No. JPA/Jakarta Persistence es una especificación; Hibernate es una implementación posible.

### “Spring Data JPA es el ORM”

No. Spring Data JPA facilita el acceso mediante repositorios. El mapeo lo realiza un proveedor JPA, como Hibernate.

### “Al usar ORM ya no necesitamos SQL”

No. El ORM genera SQL y se diagnostica mejor cuando se sabe leerlo. Además, hay consultas y necesidades que requieren escribir SQL directamente.

### “Una entidad es lo mismo que un formulario”

No necesariamente. El formulario recibe datos de una petición; una entidad representa información persistente con identidad. Pueden compartir campos, pero tienen propósitos distintos.

### “`Model` de Spring es el modelo de base de datos”

No. El `Model` MVC transporta atributos a una vista durante una petición.

### “Guardar una entidad significa escribir SQL en ese instante”

No siempre. El proveedor puede posponer la sincronización hasta el flush; la confirmación depende de la transacción.

### “El ORM hace que las consultas sean siempre eficientes”

No. Las consultas deben entenderse, medirse y mejorarse cuando sea necesario.

### “Si funciona con mi base local, funcionará igual con cualquier motor”

No. JPA y Hibernate ayudan a la portabilidad, pero cada motor tiene capacidades, tipos y comportamiento propios.

## 16. Ruta de estudio

Para estudiar el tema en orden, sigue esta secuencia:

1. **Título y propósito:** por qué se introduce ORM después de conocer SQL y JDBC.
2. **Dos representaciones:** tabla/fila/columna frente a clase/objeto/atributo.
3. **El problema del mapeo:** qué trabajo se repite al convertir resultados en objetos.
4. **Qué significa ORM:** correspondencia conceptual entre ambos lados.
5. **Qué automatiza y qué no:** beneficios y responsabilidades que siguen siendo nuestras.
6. **El mapa de nombres:** JDBC, ORM, JPA, Hibernate y Spring Data JPA, con responsabilidades distintas.
7. **Una entidad y su identidad:** registro persistente frente a objeto temporal.
8. **Las cuatro acepciones de modelo:** dominio, entidad, DTO y `Model` MVC.
9. **La entidad gestionada:** el contexto de persistencia como entorno de seguimiento.
10. **Estados de entidad:** nueva, gestionada, separada y eliminada.
11. **Cambios y sincronización:** dirty checking, flush y commit.
12. **Lectura y escritura:** recorrido conceptual de una petición de persistencia.
13. **Ventajas y costes:** evitar presentar ORM como magia o como solución universal.
14. **Persistencia y consultas:** por qué SQL sigue siendo necesario.
15. **Temas posteriores:** configuración, entidad sencilla, consultas, relaciones y transacciones.
16. **Repaso y comprobación:** preguntas breves de clasificación y explicación.

## 17. Preguntas de comprobación

1. ¿Qué diferencia hay entre una especificación y una implementación?
2. ¿Cuál de JDBC, JPA, Hibernate y Spring Data JPA ejecuta el mapeo en una configuración habitual?
3. ¿Por qué una entidad y un DTO de formulario pueden contener datos parecidos y seguir siendo objetos distintos?
4. ¿Qué representa el `Model` que un controlador entrega a Thymeleaf?
5. ¿Qué significa que una entidad esté gestionada?
6. ¿Qué diferencia conceptual hay entre flush y commit?
7. Si el ORM genera SQL, ¿por qué necesitamos saber leerlo?
8. Nombra una ventaja y un coste de utilizar un ORM.
9. ¿Qué decisiones no debería tomar automáticamente un ORM por el equipo?
10. ¿Por qué se recomienda empezar por una entidad sin relaciones?

## 18. Vocabulario esencial

| Término | Explicación inicial |
|---|---|
| ORM | Mapeo entre objetos y estructuras relacionales, y herramientas que lo realizan |
| Entidad | Objeto persistente con identidad |
| Identidad | Criterio que permite distinguir o reconocer un registro persistente |
| JPA / Jakarta Persistence | Especificación Java para persistencia de entidades |
| Hibernate ORM | Implementación ORM y proveedor JPA |
| Spring Data JPA | Capa de Spring que simplifica repositorios sobre JPA |
| Contexto de persistencia | Entorno que mantiene entidades gestionadas y sigue sus cambios |
| Entidad gestionada | Entidad que pertenece al contexto actual de persistencia |
| Detached / separada | Entidad con identidad persistente, pero fuera del contexto actual |
| Dirty checking | Detección de cambios en entidades gestionadas |
| Flush | Sincronización de cambios pendientes con la base de datos |
| Commit | Confirmación de una transacción |
| DTO | Objeto que transporta datos, por ejemplo, los recibidos de un formulario |
| `Model` MVC | Contenedor de atributos que Spring MVC entrega a una vista |
| Dialecto | Conocimiento que permite al proveedor adaptar SQL a un motor de base de datos |

## 19. Qué debes recordar

> JPA define un contrato para persistir entidades; Hibernate puede implementarlo y realizar el mapeo; Spring Data JPA simplifica operaciones mediante repositorios. El ORM automatiza parte de la traducción entre objetos y tablas, pero el diseño del modelo, las consultas, las transacciones y el conocimiento de SQL siguen siendo responsabilidad del equipo.

El siguiente paso será trabajar con un modelo sencillo, sin relaciones. Las relaciones llegarán después de que conozcas el mapeo básico y las consultas iniciales.
