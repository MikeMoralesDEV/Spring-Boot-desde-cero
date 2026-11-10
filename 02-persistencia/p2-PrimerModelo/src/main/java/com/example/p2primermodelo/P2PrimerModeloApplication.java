package com.example.p2primermodelo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de P2 Primer Modelo.
 *
 * Spring Boot configura JPA/Hibernate y detecta la entidad Recurso.
 * Hibernate crea el esquema al arrancar según las propiedades de la aplicación.
 */
@SpringBootApplication
public class P2PrimerModeloApplication {

	public static void main(String[] args) {
		SpringApplication.run(P2PrimerModeloApplication.class, args);
	}
}
