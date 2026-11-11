# Drivers de bases de datos para Spring Boot

En las primeras prácticas de persistencia conviene trabajar con un único motor de base de datos. Para esta etapa elegimos **MariaDB** como motor de referencia y conectaremos Spring Boot a una instancia real.

La elección no significa que MariaDB sea siempre mejor que MySQL o PostgreSQL. Los tres son motores relacionales capaces de trabajar con Spring Boot. La idea es reducir diferencias de instalación y configuración mientras aprendemos el recorrido completo de los datos en una aplicación.

## Qué aporta MariaDB en este momento

- **Es un servidor de base de datos real.** La aplicación se conecta a un proceso independiente mediante una URL JDBC. Esto permite distinguir con claridad la aplicación Spring Boot del servidor que almacena los datos.
- **Encaja con lo que ya sabemos.** Podemos seguir usando SQL, tablas, claves, tipos y restricciones; ahora observaremos cómo una aplicación accede a ese esquema.
- **Se integra con Java y Spring Boot.** Se añade el controlador JDBC de MariaDB al proyecto y se configura el `DataSource` con una URL, usuario y contraseña. Hibernate puede utilizarse como proveedor de JPA.
- **Permite centrarse en una configuración común.** El grupo puede compartir el mismo motor y recibir instrucciones, ejemplos y ayuda coherentes, en vez de resolver desde el principio diferencias entre tres servidores.
- **Es una elección práctica, no una dependencia de Hibernate.** El modelo JPA y las capas de la aplicación no deberían depender de detalles innecesarios del motor; cuando una característica sí dependa de él, lo identificaremos.

## Comparación dentro de un proyecto Spring Boot

| Motor | Controlador JDBC habitual | URL JDBC habitual | Qué supone para la práctica |
|---|---|---|---|
| **MariaDB** | `org.mariadb.jdbc:mariadb-java-client` | `jdbc:mariadb://servidor:3306/base` | Motor de referencia de esta fase. Facilita que el grupo siga una configuración común y practique conexión a un servidor real. |
| **MySQL** | `com.mysql:mysql-connector-j` | `jdbc:mysql://servidor:3306/base` | También tiene integración madura con Spring Boot e Hibernate. Comparte muchos conceptos con MariaDB, pero no se debe asumir que ambos motores son idénticos ni que sus controladores o todas sus características son intercambiables. |
| **PostgreSQL** | `org.postgresql:postgresql` | `jdbc:postgresql://servidor:5432/base` | También funciona muy bien con Spring Boot e Hibernate. Tiene diferencias propias en tipos, funciones, SQL y algunas decisiones de esquema; esas diferencias son más fáciles de estudiar cuando ya se domina el flujo general de persistencia. |

Spring Boot configura la conexión mediante propiedades como `spring.datasource.url`, `spring.datasource.username` y `spring.datasource.password`. El controlador JDBC permite la comunicación con el motor; JPA define el mapeo objeto-relacional y Hibernate lo implementa en la aplicación. Elegir MariaDB **no** significa que Spring Boot o JPA solo funcionen con ese motor.

## ¿MariaDB y MySQL son lo mismo?

No. MariaDB nació como una bifurcación de MySQL y conserva una amplia compatibilidad en usos comunes. Por eso resulta sencillo encontrar conceptos y herramientas familiares al pasar de uno a otro. Sin embargo, ambos proyectos han evolucionado por separado: pueden diferir en versiones, funcionalidades, valores predeterminados, tipos, funciones, comportamiento del servidor y compatibilidad de controladores.

Para una aplicación sencilla, las diferencias pueden no ser visibles al principio. En una aplicación real se debe comprobar la compatibilidad de la versión elegida y probar el SQL y el esquema sobre el motor concreto de producción.

## ¿Por qué no usar PostgreSQL desde el primer día?

PostgreSQL es una opción excelente para aplicaciones Spring Boot y no se descarta para proyectos posteriores. Sus características y diferencias respecto a MariaDB son valiosas, pero comparar motores no es el objetivo de la primera aplicación persistente. Al empezar con uno solo, podemos dedicar la atención a entender la conexión, el repositorio, el servicio, el controlador y la vista.

Una vez comprendido ese recorrido, cambiar o comparar el motor será un ejercicio mucho más útil: podremos separar qué partes son conceptos comunes de Spring/JPA y cuáles dependen del servidor concreto.

## Qué no garantiza usar JPA

JPA ayuda a expresar el mapeo entre clases y tablas de forma relativamente independiente del motor, pero no elimina todas las diferencias. Pueden depender del motor:

- los tipos SQL disponibles y su correspondencia;
- la sintaxis de consultas nativas y funciones específicas;
- la generación de identificadores y ciertos detalles del DDL;
- las restricciones, valores predeterminados, índices y reglas de migración;
- el comportamiento de collations, fechas y horas, o valores booleanos.

Por eso Hibernate no sustituye el conocimiento de SQL ni convierte automáticamente una aplicación en compatible con cualquier base de datos. Si se cambia de motor, hay que revisar la configuración, el esquema y las pruebas.

## Resumen

Usamos MariaDB como **punto de partida común**: un servidor real, conexión JDBC explícita y una integración habitual con Spring Boot y Hibernate. Más adelante podremos comparar con MySQL y PostgreSQL sobre una aplicación ya entendida, en vez de aprender simultáneamente el flujo de persistencia y las particularidades de varios motores.
