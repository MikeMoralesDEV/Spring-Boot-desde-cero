# P2: Primer modelo y mapeo ORM

Aplicación Spring Boot didáctica para ver cómo una clase Java sencilla se mapea a una tabla con las anotaciones de Jakarta Persistence e Hibernate ORM.

El único resultado al iniciar la aplicación es que Hibernate cree la base local H2 y la tabla `recursos`. La base se guarda en archivos dentro de `data`. No hay servidor web ni página de inicio.

La práctica está limitada intencionadamente al **mapeo y la creación del esquema**: no hay controlador, interfaz de repositorio, servicio, DAO, formulario, carga de filas ni operaciones de consulta o modificación. Spring Boot configura JPA e Hibernate automáticamente a partir de las dependencias y propiedades; no se añade una clase de configuración propia.

## Lombok

Lombok genera métodos Java repetitivos durante la compilación:

- `@Getter` genera getters para los atributos.
- `@Setter` genera setters.
- `@NoArgsConstructor` genera el constructor sin argumentos que JPA necesita para reconstruir entidades.

`@Data` sí incluye `@Getter` y `@Setter`, pero también genera `equals`, `hashCode` y `toString`; **no** genera el constructor vacío. En entidades conviene elegir las anotaciones necesarias: una igualdad generada a partir de atributos mutables o de un identificador que aparece al persistir puede dar resultados inesperados, y un `toString` generado puede ser problemático cuando el modelo incorpora relaciones. Por eso esta clase usa `@Getter`, `@Setter` y `@NoArgsConstructor`, sin `@Data`.

El proyecto configura Lombok como procesador de anotaciones de Maven. Algunos IDE requieren habilitar el procesamiento de anotaciones para mostrar correctamente los métodos generados durante el análisis del código.

## Creación del esquema

La propiedad `spring.jpa.hibernate.ddl-auto=create` pide a Hibernate crear las tablas mapeadas al arrancar. En este proyecto la URL de H2 crea también la base local si todavía no existe. **`create` puede eliminar y volver a crear tablas, por lo que los datos anteriores no se conservan.** Se usa aquí para observar la creación automática; no es una configuración para una aplicación con datos que se quieran conservar.

H2 crea la base de datos local en archivo sin instalar ni iniciar un servidor externo. Aunque la dependencia de Spring Boot se llama `spring-boot-starter-data-jpa`, el proyecto no declara ni utiliza repositorios Spring Data: se usa para la integración y auto-configuración de JPA/Hibernate. No se ha añadido una consola web ni configuración para motores alternativos.

## Ejecutar

Desde esta carpeta, inicia la aplicación con:

```powershell
.\mvnw.cmd spring-boot:run
```

Al iniciar, revisa los registros de Hibernate para observar la conexión y la creación de la tabla. La aplicación termina de arrancar y se queda activa; no ofrece páginas web ni incluye pruebas automatizadas.
