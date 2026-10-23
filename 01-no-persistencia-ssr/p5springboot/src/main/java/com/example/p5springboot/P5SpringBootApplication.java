package com.example.p5springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de P5.
 */
@SpringBootApplication
public class P5SpringBootApplication {

	/**
	 * Arranca Spring Boot y su servidor web integrado.
	 *
	 * @param args argumentos opcionales de inicio
	 */
	public static void main(String[] args) {
		SpringApplication.run(P5SpringBootApplication.class, args);
	}
}
