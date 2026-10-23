package com.example.p3springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de P3. Spring Boot configura el servidor web y Thymeleaf,
 * y detecta controladores en este paquete y sus subpaquetes.
 */
@SpringBootApplication
public class P3SpringBootApplication {

	/**
	 * Inicia el contexto de Spring y el servidor web integrado.
	 *
	 * @param args argumentos opcionales de inicio
	 */
	public static void main(String[] args) {
		SpringApplication.run(P3SpringBootApplication.class, args);
	}
}
