package com.example.p4springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de P4. Spring Boot configura MVC, Thymeleaf y validación.
 */
@SpringBootApplication
public class P4SpringBootApplication {

	/**
	 * Inicia la aplicación y su servidor web integrado.
	 *
	 * @param args argumentos opcionales de inicio
	 */
	public static void main(String[] args) {
		SpringApplication.run(P4SpringBootApplication.class, args);
	}
}
