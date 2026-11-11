# Proyecto de comprobación — Apartado 1

Este proyecto es el punto de partida proporcionado por el profesorado. Añade las entidades descritas en `ENUNCIADO.md` dentro del paquete:

`com.example.u4p2.apartado1.modelo`

La propiedad `spring.jpa.hibernate.ddl-auto` está vacía intencionadamente. Completa con `create` antes de iniciar la aplicación o ejecutar las pruebas. No uses una base que contenga datos importantes.

Las pruebas JUnit comprueban las anotaciones del modelo y el esquema que Hibernate genera. Ejecuta el informe desde esta carpeta:

```powershell
.\mvnw.cmd clean test "-Dmaven.test.failure.ignore=true" surefire-report:report-only
```

Informe: `target\reports\surefire.html`.
