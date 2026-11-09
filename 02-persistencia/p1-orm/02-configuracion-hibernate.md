# Configuración de persistencia e Hibernate

## Guía de estudio

En esta guía aprenderás qué piezas intervienen al conectar una aplicación Spring Boot con una base de datos relacional local, cómo se relacionan y qué significan los modos de gestión automática del esquema de Hibernate.

**Esta guía no incluye código Java ni bloques de configuración para copiar.** Su propósito es que entiendas las decisiones y sus efectos antes de aplicarlas en un proyecto.

## 1. Objetivos

Al terminar esta guía, deberías poder:

- Explicar cómo se relacionan Spring Boot, JPA, Hibernate, el pool de conexiones, un driver JDBC y la base de datos.
- Identificar qué información necesita una aplicación para conectarse a una base local.
- Distinguir la configuración de conexión de la configuración de mapeo y de la gestión del esquema.
- Explicar los modos `none`, `validate`, `update`, `create` y `create-drop`.
- Elegir un modo adecuado para una práctica local y justificar por qué no debe confundirse con una estrategia de migraciones.
- Reconocer los riesgos de dejar a Hibernate modificar un esquema con datos que se quieren conservar.
- Diagnosticar de forma ordenada los errores habituales de conexión y de mapeo.

## 2. Antes de configurar: identifica las piezas

En una aplicación habitual, la ruta simplificada es:

**Aplicación Spring → Spring Data JPA (si se usa) → JPA → Hibernate → JDBC y su driver → base de datos**

No todas las aplicaciones necesitan todas las capas de abstracción. La ruta muestra una configuración frecuente de Spring Boot con Spring Data JPA y Hibernate.

| Pieza | Responsabilidad |
|---|---|
| Aplicación | Define el modelo, las operaciones y las reglas |
| Spring Boot | Crea y configura componentes a partir de las dependencias y propiedades |
| Spring Data JPA | Facilita el acceso a entidades mediante repositorios, si se incorpora |
| Jakarta Persistence | Define el contrato de persistencia utilizado por la aplicación |
| Hibernate ORM | Implementa el mapeo y prepara operaciones para la base de datos |
| Pool de conexiones | Reutiliza conexiones para no abrir una nueva por cada operación |
| Driver JDBC | Traduce las llamadas de Java al protocolo que entiende el motor |
| Base de datos | Almacena los datos y aplica restricciones y transacciones |

Spring Boot puede automatizar la creación de varias piezas, pero no puede adivinar credenciales, qué servidor local se está usando ni qué datos deben conservarse.

## 3. Cuándo empieza a ser necesaria esta configuración

En la fase anterior, el controlador podía devolver datos preparados en memoria. Para recuperar datos persistidos, la aplicación necesita poder abrir una conexión con la base de datos y disponer de un mapeo compatible con el esquema.

Antes de arrancar una aplicación persistente, deben estar claros estos puntos:

1. Qué motor relacional se utilizará en ese entorno.
2. Si el servicio de base de datos está instalado, iniciado y accesible.
3. Qué base de datos o esquema utilizará la aplicación.
4. Qué usuario puede conectarse y con qué permisos.
5. Qué driver corresponde al motor.
6. Si las entidades y el esquema esperado coinciden.
7. Quién es responsable de crear o modificar el esquema.

La respuesta a la última pregunta determina si se permite que Hibernate genere o ajuste el esquema, si solo debe comprobarlo o si las modificaciones se gestionan por otra herramienta.

## 4. Dependencias: driver y persistencia

Para trabajar con JPA en Spring Boot se incorpora la dependencia de persistencia apropiada; esta permite que Boot configure la infraestructura correspondiente. También se necesita un driver para el motor concreto.

El driver suele ser una dependencia de ejecución: la aplicación necesita cargarlo para conectarse, pero el código de dominio no debería depender de las clases específicas del driver.

### El motor es una elección por entorno

MySQL, MariaDB, PostgreSQL y otros motores pueden servir para los ejercicios, siempre que:

- La base de datos esté disponible localmente.
- Se use el driver correspondiente.
- La URL y las propiedades sean válidas para ese motor.
- Se pruebe la aplicación contra el motor elegido.
- Se revisen las diferencias si el esquema, las consultas o el SQL generado dependen de capacidades específicas.

Cambiar de motor no consiste siempre en cambiar una única cadena. El driver, la URL, las credenciales, los tipos y algunas opciones pueden variar.

### Evitar dependencias innecesarias

No se deben declarar varios drivers “por si acaso” sin una razón. Mantener solo el driver usado por el proyecto hace más clara la configuración y evita confusiones sobre qué motor se espera.

## 5. Información necesaria para una conexión

La aplicación normalmente necesita conocer:

### URL de conexión

Identifica el motor/protocolo, el servidor o nombre de host, el puerto y la base de datos o esquema de destino. Su sintaxis depende del driver y del motor.

### Usuario y contraseña

Identifican una cuenta de la base de datos. La cuenta debe tener los permisos necesarios para las operaciones previstas. No es recomendable usar una cuenta administrativa del motor para una aplicación corriente.

### Driver

La dependencia del driver proporciona la implementación que permite a Java comunicarse con ese motor. Spring Boot suele inferir la clase del driver desde la URL y las dependencias, por lo que fijarla manualmente puede no ser necesario.

### Opciones adicionales

Según el motor y el proyecto, pueden ser relevantes parámetros de conexión, codificación, zona horaria, SSL o límites de tiempo. No conviene añadir parámetros sin entender su efecto ni copiar una URL ajena sin comprobar que corresponde al entorno.

## 6. Configuración de la aplicación y credenciales

Spring Boot permite declarar propiedades en los recursos de la aplicación o proporcionarlas desde el entorno en que se ejecuta. La elección debe evitar que las credenciales personales queden publicadas en el repositorio.

### Recomendaciones

- Separar propiedades relativamente estables —por ejemplo, qué motor se usa— de secretos como contraseñas.
- No guardar contraseñas personales ni credenciales reales en archivos versionados.
- Usar variables de entorno, una configuración local ignorada por Git o un mecanismo de secretos adecuado al entorno.
- Documentar qué valores debe completar cada estudiante sin publicar valores sensibles.
- Mantener configuraciones distintas para desarrollo, pruebas y despliegue cuando sus necesidades difieran.
- No asumir que una configuración de la máquina docente funciona automáticamente en la de cada estudiante.

### Configuración por entornos

Una práctica puede tener una configuración de desarrollo local y otra para pruebas. Los perfiles de Spring ayudan a seleccionar configuraciones, pero no protegen por sí solos un secreto si este se escribe en un archivo compartido y versionado.

Un archivo de perfil también puede terminar en Git; por eso hay que revisar qué contiene y cómo se distribuye.

## 7. Datasource y pool de conexiones

El **datasource** representa el origen de conexiones que usa la aplicación. En vez de abrir y cerrar una conexión física desde cero por cada operación, normalmente se utiliza un **pool** que conserva conexiones disponibles para reutilizarlas.

El pool:

- Reduce el coste de crear conexiones repetidamente.
- Limita cuántas conexiones puede abrir la aplicación al mismo tiempo.
- Administra la disponibilidad y el tiempo de vida de las conexiones.

El pool no sustituye al driver ni a la base de datos. Una URL incorrecta, un servidor detenido o credenciales inválidas siguen impidiendo la conexión.

Para ejercicios iniciales se puede aceptar la configuración predeterminada del pool que proporciona Spring Boot. Ajustar límites y tiempos es un tema posterior: no hay que modificar valores de rendimiento sin una necesidad medida.

## 8. Dialecto y particularidades del motor

Un **dialecto** describe cómo expresar operaciones de persistencia en el lenguaje que entiende un motor concreto. Hibernate lo utiliza para adaptar parte del SQL que genera.

En versiones actuales de Hibernate, el dialecto puede detectarse a partir de la conexión y de la información del driver. Por tanto, no es correcto recomendar como regla universal que se configure manualmente en toda aplicación.

Antes de declarar un dialecto:

1. Comprobar si la aplicación arranca y detecta correctamente el motor.
2. Revisar la documentación de la versión de Hibernate utilizada.
3. Añadir una configuración explícita solo si existe una razón concreta.
4. Asegurarse de que el dialecto declarado corresponde al motor y a la versión que se ejecutan.

Configurar un dialecto incorrecto puede llevar a SQL incompatible o a advertencias difíciles de interpretar.

## 9. Tres asuntos que no se deben confundir

### Configuración de conexión

Indica cómo llegar a la base de datos: URL, cuenta, driver y opciones de conexión.

### Mapeo de entidades

Indica cómo las clases persistentes y sus atributos se corresponden con el esquema relacional. Depende de las entidades y de la configuración JPA/Hibernate.

### Gestión del esquema

Determina si Hibernate no hace nada con el esquema, lo comprueba o intenta crearlo/modificarlo.

La aplicación puede conectarse correctamente y aun así tener un mapeo inválido. También puede tener entidades bien mapeadas y no poder conectar. Esos fallos pertenecen a etapas distintas del arranque.

## 10. Qué significa gestionar el esquema

El **esquema** es la estructura de la base de datos: tablas, columnas, tipos, restricciones, claves, índices y otros elementos.

Hibernate puede usar la información del mapeo para generar acciones de esquema al iniciar. Esto resulta cómodo en desarrollo y en ejercicios iniciales, pero las acciones automáticas tienen que elegirse con cuidado.

La propiedad que se suele encontrar en proyectos Spring Boot para decidir esta conducta es `spring.jpa.hibernate.ddl-auto`. El nombre `ddl-auto` alude a la gestión automática de las instrucciones de definición del esquema. Los valores habituales son `none`, `validate`, `update`, `create` y `create-drop`.

Los nombres describen intenciones generales, no prometen que todos los cambios complejos del esquema puedan resolverse con seguridad.

## 11. Modos de gestión automática

| Modo | Intención general | ¿Modifica el esquema? | Uso orientativo |
|---|---|---:|---|
| `none` | No gestionar el esquema desde Hibernate | No | Cuando otro mecanismo es responsable del esquema |
| `validate` | Comprobar que el esquema existente concuerda con el mapeo | No | Verificación al arrancar sin pedir cambios automáticos |
| `update` | Intentar ajustar el esquema existente al mapeo | Sí, intenta cambios | Desarrollo local controlado, con datos prescindibles y revisión |
| `create` | Crear el esquema al arrancar desde el mapeo | Sí; puede eliminar/recrear objetos | Pruebas o ejercicios donde se acepta reconstruir el esquema |
| `create-drop` | Crear al arrancar y eliminar el esquema al cerrar la aplicación | Sí, al inicio y al cierre | Pruebas o demostraciones completamente desechables |

El efecto concreto puede depender de la versión de Hibernate, el motor y el esquema existente. Antes de usar cualquier modo destructivo, hay que comprobar qué base de datos apunta la aplicación.

## 12. Explicación detallada de cada modo

### `none`

Hibernate no crea, modifica ni valida el esquema como parte de esta opción. Se presupone que el esquema ya existe o que otra herramienta se ocupa de él.

Es adecuado cuando la aplicación no debe intentar alterar la estructura. La ausencia de gestión automática no significa que no haya que crear o mantener el esquema; significa que esa responsabilidad está en otra parte.

### `validate`

Hibernate compara el mapeo con la estructura disponible y falla al arrancar si detecta incompatibilidades relevantes. No intenta repararlas ni crear tablas.

Puede ayudar a detectar que el código espera una columna que no existe o que el esquema no corresponde al mapeo. No reemplaza una prueba funcional ni asegura que los tipos, datos y reglas de negocio sean correctos.

### `update`

Hibernate intenta modificar el esquema existente para que se ajuste al mapeo actual. Es cómodo durante aprendizaje y desarrollo porque reduce pasos manuales al cambiar el modelo.

Pero **no es una herramienta fiable de migraciones**:

- No expresa claramente qué cambio se desea realizar ni por qué.
- No proporciona por sí mismo un historial versionado y revisable de cambios.
- No puede inferir con seguridad la intención semántica de un cambio.
- Una columna renombrada puede interpretarse como una columna eliminada y otra añadida.
- Cambiar un tipo o una restricción puede tener consecuencias para los datos existentes.
- Puede no eliminar o transformar estructuras de la manera que el equipo necesita.
- El resultado puede diferir entre versiones, motores y estados previos del esquema.

Por ello, `update` se limita a entornos locales controlados. No se debe usar con datos importantes ni como estrategia de despliegue.

### `create`

Hibernate construye el esquema a partir del mapeo al arrancar. Para crear una estructura limpia, normalmente elimina o reemplaza objetos existentes administrados por esa generación. En consecuencia, los datos pueden perderse.

Se utiliza cuando se acepta comenzar de cero, por ejemplo en una base de datos dedicada a una prueba desechable. No se debe apuntar a una base local que contenga información que se quiera conservar.

### `create-drop`

Se comporta como una generación limpia al iniciar y elimina el esquema generado cuando termina el contexto de la aplicación. Es útil para pruebas efímeras que no deben dejar estructura ni datos tras finalizar.

No es apropiado si se espera que los datos sobrevivan al cierre de la aplicación.

## 13. Advertencia sobre valores predeterminados

No conviene depender de un valor implícito de `ddl-auto`. Los valores predeterminados de Spring Boot pueden variar según si detecta una base de datos embebida y según las condiciones de configuración.

Para una práctica didáctica:

- Explicitar la decisión hace visible quién controla el esquema.
- Comprobar el motor al que apunta la URL evita borrar datos por accidente.
- Documentar el comportamiento esperado reduce sorpresas entre máquinas.
- Revisar el resultado real es más seguro que deducirlo solo por el nombre de la propiedad.

La guía de cada ejercicio debe decir claramente si los datos sobreviven al reinicio y si el esquema se crea, valida, actualiza o se gestiona por otro medio.

## 14. Por qué `update` no equivale a migrar

Una **migración** es un cambio intencionado y versionado del esquema, expresado de forma que se pueda revisar, aplicar en orden y compartir entre entornos.

Por ejemplo, el equipo puede necesitar cambiar el significado de una columna, combinar datos, rellenar un valor para registros antiguos o realizar un cambio en dos pasos para conservar información. El ORM no puede deducir la intención funcional a partir de dos versiones de una clase.

Una herramienta de migraciones permite escribir y revisar esos cambios explícitamente. Ese tema se deja como ampliación avanzada, pero es importante que desde el comienzo quede claro que:

> `update` ayuda a experimentar en local; no es una alternativa segura a las migraciones versionadas.

## 15. Recomendación de uso por etapa

La elección depende del objetivo del ejercicio y del valor de los datos.

### Exploración inicial

Para empezar, puede ser útil observar cómo se crea un esquema desde un modelo sencillo. Si los datos se pueden borrar y la base está dedicada a la práctica, se puede escoger un modo de creación limpia, siempre entendiendo su efecto.

### Desarrollo donde se conservan datos entre reinicios

Se requiere una opción que no borre el esquema en cada arranque. `update` puede resultar cómodo para cambios pequeños en una base local prescindible, pero hay que advertir que no protege frente a cambios semánticos ni sustituye migraciones.

### Esquema creado fuera de Hibernate

Usar validación o no gestionar el esquema, según se quiera que el arranque compruebe la coincidencia o que otra herramienta controle completamente ese aspecto.

### Datos importantes o despliegue

No usar `create` ni `create-drop`. No confiar en `update` para cambios de esquema de producción. Utilizar una estrategia de migración explícita y controlada.

No hay una configuración universalmente correcta: primero se decide quién es responsable del esquema y qué valor tienen los datos.

## 16. Generar y mostrar SQL

Hibernate puede configurarse para mostrar el SQL que ejecuta, lo que ayuda a conectar las operaciones del modelo con las consultas reales.

Es útil para:

- Confirmar qué consulta se ejecuta ante una operación.
- Entender cuándo se produce una inserción o actualización.
- Observar consultas repetidas.
- Relacionar una entidad con tablas y columnas.
- Diagnosticar diferencias entre lo que se esperaba y lo que se envió al motor.

Hay que tener en cuenta:

- El SQL mostrado puede contener datos de la aplicación; no debe publicarse sin revisarlo.
- Registrar SQL detallado en producción puede generar mucho volumen y exponer información sensible.
- Mostrar SQL no equivale a mostrar todos los valores de parámetros ni explica por sí solo el plan de ejecución.
- La salida exacta depende de las versiones y del sistema de logging.

Para el aula, es preferible activar la visualización solo donde ayude al objetivo didáctico y explicar cómo apagarla.

## 17. Secuencia de arranque y diagnóstico

Al iniciar la aplicación, una secuencia simplificada es:

1. Spring Boot carga dependencias y propiedades.
2. Configura el datasource y el pool.
3. El driver intenta establecer la conexión.
4. JPA/Hibernate descubre las entidades y lee el mapeo.
5. La estrategia de esquema se aplica, si corresponde.
6. El contexto de persistencia queda disponible para las operaciones.
7. La aplicación termina de arrancar y atiende peticiones.

Este orden ayuda a clasificar errores. Una excepción al abrir la conexión no se arregla modificando una anotación de entidad; un error de mapeo no se arregla cambiando la contraseña.

### Preguntas para revisar en orden

1. ¿Está iniciado el servicio del motor?
2. ¿El host y el puerto son correctos?
3. ¿Existe la base o esquema indicado?
4. ¿Son correctos la URL y los parámetros propios del motor?
5. ¿Está presente el driver correcto?
6. ¿El usuario puede conectarse y tiene los permisos necesarios?
7. ¿El mapeo de entidades es válido?
8. ¿La estrategia de esquema corresponde al estado real de la base?
9. ¿Hay un conflicto de puerto, versión, dialecto o compatibilidad?
10. ¿Qué indica la primera causa de la excepción, no solo las últimas líneas del mensaje?

## 18. Errores frecuentes

### El servidor no está iniciado

La aplicación no puede alcanzar la dirección de la base. Confirmar primero el estado del servicio local y el puerto configurado.

### URL incorrecta

Puede tener un protocolo, nombre de base, host, puerto o parámetro que no corresponde al motor. Compararla con la documentación del driver instalado.

### Driver ausente o equivocado

Spring no puede crear una conexión compatible si no encuentra el driver adecuado o si el proyecto declara otro motor.

### Credenciales o permisos incorrectos

Una cuenta puede existir y aun así no tener permiso sobre la base de datos o para las operaciones que se intentan.

### La base de datos no existe

Configurar una URL no siempre crea automáticamente la base de datos del servidor. Puede ser necesario crearla previamente con las herramientas del motor y conceder permisos.

### El esquema no coincide

El modo `validate` puede detectar discrepancias al arrancar. Revisar las entidades y la estructura real; no activar `create` a ciegas si los datos importan.

### Tabla o columna con nombre inesperado

El proveedor puede derivar nombres a partir del mapeo o aplicar reglas del motor sobre mayúsculas y minúsculas. Revisar el esquema generado y el mapeo esperado.

### El esquema parece desaparecer

Comprobar si se ha elegido `create` o `create-drop`, si se está usando una base embebida o una base en memoria, y si la URL cambia entre ejecuciones.

### Contraseña comprometida en Git

Eliminarla del archivo actual no necesariamente la elimina del historial. Rotar la credencial y seguir el procedimiento del repositorio; no basta con borrar el texto del último cambio.

## 19. Recomendaciones de seguridad y cuidado de datos

- Usar una base o esquema dedicado para ejercicios que puedan recrearse.
- No probar modos destructivos sobre una base de datos compartida ni con información personal.
- Evitar cuentas administrativas del motor para ejecutar la aplicación.
- Conceder solo los permisos necesarios al usuario de aplicación.
- No publicar contraseñas, cadenas de conexión con secretos ni registros que contengan información sensible.
- Hacer copia de seguridad antes de experimentar con cambios cuyo efecto no se conoce.
- Confirmar la URL activa antes de iniciar la aplicación con `create` o `create-drop`.
- No considerar un modo local cómodo como una configuración adecuada para producción.

## 20. Lista de comprobación antes de arrancar

- [ ] He elegido un motor local y sé cómo iniciar su servicio.
- [ ] Tengo el driver correcto para ese motor.
- [ ] La URL identifica la base de datos adecuada.
- [ ] Las credenciales se obtienen de una configuración local segura.
- [ ] La base existe y el usuario tiene permisos suficientes.
- [ ] Sé cómo se crea el esquema y qué modo de `ddl-auto` se ha elegido.
- [ ] He confirmado si los datos pueden borrarse.
- [ ] No he configurado una contraseña real en un archivo que se versionará.
- [ ] El mapeo de entidades es compatible con el esquema esperado.
- [ ] Sé dónde revisar el primer error de conexión, mapeo o esquema.

## 21. Ruta de estudio

1. **Sigue el recorrido de componentes:** desde la aplicación hasta la base de datos.
2. **Repasa las piezas de conexión:** motor, base, usuario, driver y URL.
3. **Separa las decisiones:** identifica qué opciones configuran conexión, mapeo o esquema.
4. **Compara los modos:** entiende `none`, `validate`, `update`, `create` y `create-drop`.
5. **Decide según los datos:** piensa qué usar si los datos se pueden borrar, deben sobrevivir o el esquema se administra fuera de Hibernate.
6. **Clasifica los errores:** determina qué capa revisar antes de cambiar propiedades al azar.
7. **Protege la configuración:** identifica qué información no se debe compartir.
8. **Comprueba que lo entendiste:** explica qué pasaría con los datos si detienes y vuelves a iniciar la aplicación.

La aplicación de estas decisiones se verá en el proyecto práctico, cuando se haya definido la entidad que se mapeará y el comportamiento del esquema que se necesita.

## 22. Preguntas de comprobación

1. ¿Qué diferencia hay entre un driver JDBC y Hibernate?
2. Si la conexión funciona, ¿queda garantizado que las entidades están correctamente mapeadas?
3. ¿Qué hace `validate` y qué no hace?
4. ¿Por qué `update` no se considera una herramienta de migraciones?
5. ¿Qué riesgo tiene `create` sobre una base que contiene información que se quiere conservar?
6. ¿En qué se diferencia `create-drop` de `create` al cerrar la aplicación?
7. ¿Por qué puede no ser necesario configurar el dialecto manualmente?
8. ¿Qué valores no deberían guardarse en un archivo compartido por Git?
9. ¿Qué comprobarías primero si aparece un error de conexión?
10. ¿Quién debe controlar el esquema cuando Hibernate está configurado con `none`?

## 23. Glosario

| Término | Significado |
|---|---|
| Datasource | Origen de conexiones que utiliza la aplicación |
| Pool de conexiones | Conjunto de conexiones reutilizables administrado por la aplicación |
| Driver JDBC | Componente que permite a Java comunicarse con un motor específico |
| Dialecto | Adaptación del SQL que Hibernate genera a las características de un motor |
| Esquema | Estructura de tablas, columnas, claves, restricciones e índices |
| DDL | Instrucciones que definen o modifican estructuras de base de datos |
| `ddl-auto` | Opción de Hibernate integrada por Spring Boot para decidir cómo actuar sobre el esquema |
| `none` | No gestionar automáticamente el esquema |
| `validate` | Comprobar el esquema sin modificarlo |
| `update` | Intentar ajustar el esquema existente al mapeo |
| `create` | Generar el esquema al iniciar, con riesgo de pérdida de estructuras/datos anteriores |
| `create-drop` | Crear el esquema al iniciar y eliminarlo al cerrar |
| Migración | Cambio explícito, versionado y revisable del esquema |

## 24. Qué debes recordar

La configuración de persistencia reúne responsabilidades distintas: conectar con el motor, interpretar el mapeo y decidir quién mantiene el esquema. Spring Boot automatiza parte del arranque; Hibernate puede generar SQL y gestionar el mapeo; **la decisión sobre qué datos conservar y cómo evolucionar el esquema sigue siendo nuestra**.
