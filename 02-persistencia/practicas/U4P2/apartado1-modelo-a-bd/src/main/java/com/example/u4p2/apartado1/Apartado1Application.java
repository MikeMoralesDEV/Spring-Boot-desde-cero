package com.example.u4p2.apartado1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación de comprobación del apartado 1.
 *
 * No contiene rutas web ni operaciones de negocio. Spring Boot localiza las
 * entidades creadas por el alumnado y Hibernate genera su esquema al arrancar.
 */
@SpringBootApplication
public class Apartado1Application {

	public static void main(String[] args) {
		SpringApplication.run(Apartado1Application.class, args);
	}
}
