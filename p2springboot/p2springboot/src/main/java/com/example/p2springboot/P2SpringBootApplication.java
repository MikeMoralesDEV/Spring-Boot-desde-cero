package com.example.p2springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de P2. Spring Boot detecta el controlador de este paquete
 * y configura el servidor web y Thymeleaf a partir de las dependencias.
 */
@SpringBootApplication
public class P2SpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(P2SpringBootApplication.class, args);
	}
}
