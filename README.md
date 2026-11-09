# Spring Boot desde cero

Repositorio de prácticas progresivas con Spring Boot, Spring MVC y Thymeleaf, organizadas por fases de aprendizaje. Cada práctica es una aplicación Maven independiente; no hay un proyecto Maven agregador ni dependencias entre prácticas.

## Fases

| Fase | Contenido | Estado |
|---|---|---|
| `01-no-persistencia-ssr` | P1–P6: aplicaciones didácticas con datos en memoria y renderizado del lado del servidor (SSR). | Disponible |
| `02-persistencia` | Introducción al ORM y primer modelo Java sencillo; configuración y persistencia se incorporarán progresivamente. | En desarrollo |
| `03-login-y-sesiones` | Prácticas para autenticación, login y gestión de sesiones. | Preparada para próximas prácticas |

Los enunciados y criterios de entrega de P2–P6 están en `01-no-persistencia-ssr`.

Cada proyecto conserva su propio `pom.xml`, wrapper de Maven, código, recursos y pruebas. Para iniciar P1 desde la raíz:

```powershell
cd .\01-no-persistencia-ssr\p1springboot
.\mvnw.cmd spring-boot:run
```

Para ejecutar las pruebas de esa práctica, desde su directorio:

```powershell
.\mvnw.cmd test
```

Las prácticas de la fase 2 se mantienen como proyectos independientes dentro de `02-persistencia`. La fase 3 queda reservada para más adelante.
