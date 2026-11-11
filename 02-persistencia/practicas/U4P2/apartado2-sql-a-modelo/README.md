# Proyecto de comprobación — Apartado 2

Este proyecto proporciona `src/main/resources/schema.sql`, el esquema SQL que debes mapear, y las pruebas JUnit. Crea las entidades en:

`com.example.u4p2.apartado2.modelo`

La propiedad `spring.jpa.hibernate.ddl-auto` está vacía intencionadamente. Completa con `validate`: el SQL se ejecuta al iniciar y Hibernate debe comprobar el mapeo sin modificar las tablas.

El SQL crea las dos tablas si aún no existen. Usa la base local exclusiva de esta práctica.

Ejecuta el informe desde esta carpeta:

```powershell
.\mvnw.cmd clean test "-Dmaven.test.failure.ignore=true" surefire-report:report-only
```

Informe: `target\reports\surefire.html`.
