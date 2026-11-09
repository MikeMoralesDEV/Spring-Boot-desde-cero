# P2: Primer modelo

Aplicación Spring Boot didáctica para conocer una clase propia y trabajar con objetos del dominio en una colección en memoria.

Esta práctica todavía no configura una base de datos ni utiliza JPA/Hibernate. No hay formularios: el controlador proporciona varios objetos `Recurso` y Thymeleaf los presenta en una página.

## Ejecutar

Desde esta carpeta, inicia la aplicación con:

```powershell
.\mvnw.cmd spring-boot:run
```

Después abre `http://localhost:8080/`.

Para ejecutar las pruebas:

```powershell
.\mvnw.cmd test
```
