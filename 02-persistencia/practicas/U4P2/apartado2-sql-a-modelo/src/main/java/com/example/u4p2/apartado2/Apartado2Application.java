package com.example.u4p2.apartado2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación de comprobación del apartado 2.
 *
 * Spring Boot ejecuta el esquema SQL proporcionado y Hibernate comprueba
 * que las entidades creadas por el alumnado se adaptan a esas tablas.
 */
@SpringBootApplication
public class Apartado2Application {

	public static void main(String[] args) {
		SpringApplication.run(Apartado2Application.class, args);
	}
}
