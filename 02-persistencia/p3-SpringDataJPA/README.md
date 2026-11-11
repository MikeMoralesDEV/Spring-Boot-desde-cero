# P3 — Spring Data JPA

Aplicación web de servidor (Spring MVC + Thymeleaf) que muestra el inventario completo de una tienda y permite registrar nuevos videojuegos. Los datos se guardan en MariaDB y permanecen después de cerrar y volver a iniciar la aplicación. El proyecto sirve para estudiar Spring Data JPA, junto con las capas de servicio y MVC que coordinan el flujo.

El proyecto sirve para observar el recorrido de una petición a través del modelo, el repositorio, el servicio, el controlador y la vista. Las capas nuevas de persistencia están comentadas ampliamente en el código.

## Requisitos

- Java 23.
- MariaDB Server en ejecución.
- Una base de datos vacía llamada `tienda_videojuegos`.

Crear la base de datos en MariaDB antes de iniciar Spring Boot:

```sql
CREATE DATABASE tienda_videojuegos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

La aplicación usa el usuario `root` y contraseña vacía si no se configura nada. Es preferible usar un usuario local limitado a esta base. Por ejemplo, desde un cliente MariaDB con permisos de administración:

```sql
CREATE USER 'p3_app'@'localhost' IDENTIFIED BY 'elige-una-clave-local';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX
    ON tienda_videojuegos.* TO 'p3_app'@'localhost';
```

Configurar las credenciales para la sesión de PowerShell antes de arrancar la aplicación:

```powershell
$env:DB_USERNAME = "p3_app"
$env:DB_PASSWORD = "elige-una-clave-local"
```

Las variables opcionales `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` sobrescriben los valores por defecto de `src/main/resources/application.properties`. No guardes contraseñas reales en ese archivo ni en Git.

## Ejecutar

Desde esta carpeta:

```powershell
.\mvnw.cmd spring-boot:run
```

Abrir [http://localhost:8080](http://localhost:8080). La pantalla carga todos los videojuegos ordenados por título y, debajo del listado, presenta el formulario para registrar uno nuevo. Al enviarlo correctamente, el navegador vuelve al inventario actualizado.

Ejecutar las pruebas unitarias:

```powershell
.\mvnw.cmd test
```

Las pruebas unitarias del servicio usan Mockito para sustituir el repositorio y las pruebas web usan MockMvc con el servicio simulado; no necesitan conectarse a MariaDB. Para ejecutar la aplicación sí hace falta que MariaDB esté iniciado y que exista la base configurada.

## Decisiones para esta práctica

- `GET /` pide el inventario al servicio y renderiza la vista Thymeleaf.
- `POST /videojuegos` valida el formulario, solicita al servicio que registre el juego y redirige al inventario.
- El repositorio extiende `JpaRepository`, que proporciona operaciones CRUD sin una implementación escrita a mano.
- `VideojuegoRepositorio` incluye `findAllOrdenadosPorTitulo()` como ejemplo de consulta JPQL propia. Es didáctica y redundante: `JpaRepository` ya ofrece `findAll()` y `findAll(Sort)`.
- El servicio concentra los casos de uso y delimita las transacciones.
- `@Transactional(readOnly = true)` en el servicio marca las consultas como de solo lectura; `registrar()` declara una transacción de escritura que sustituye esa configuración. Spring gestiona inicio y final de la transacción alrededor de la llamada al método. `readOnly` expresa intención y puede servir como pista de optimización; no es una barrera de seguridad ni garantiza por sí solo que no haya escrituras.
- `spring.jpa.hibernate.ddl-auto=update` permite crear/ajustar la tabla en este ejercicio y conserva los datos entre reinicios. Es una comodidad didáctica, no una estrategia recomendada para gestionar el esquema en producción.
- Todavía no se implementan edición, borrado, relaciones entre entidades, autenticación ni control de roles.

## Recorrido del código

1. `modelo/Videojuego.java`: clase persistente y mapeo de columnas.
2. `repositorio/VideojuegoRepositorio.java`: interfaz de acceso a datos Spring Data JPA.
3. `servicio/VideojuegoServicio.java`: listado, orden y registro dentro de transacciones.
4. `controlador/VideojuegoControlador.java`: rutas GET/POST, validación y preparación del modelo MVC.
5. `dto/NuevoVideojuegoFormulario.java`: datos permitidos y reglas del formulario; no es una entidad.
6. `templates/inventario.html`: listado y formulario renderizados en el servidor.
7. `static/css/inventario.css`: diseño visual responsive con identidad de tienda retro.
