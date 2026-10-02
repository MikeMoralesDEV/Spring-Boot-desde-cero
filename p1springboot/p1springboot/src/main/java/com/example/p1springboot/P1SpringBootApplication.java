package com.example.p1springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación Spring Boot.
 *
 * La anotación {@code @SpringBootApplication} activa la configuración
 * automática de Spring Boot, registra esta clase como configuración
 * y busca componentes (por ejemplo, controladores) en este paquete
 * y en sus subpaquetes.
 */
@SpringBootApplication
public class P1SpringBootApplication {

	/**
	 * Arranca Spring Boot, crea el contexto de la aplicación y pone en marcha
	 * el servidor web integrado para que pueda recibir peticiones HTTP.
	 *
	 * @param args argumentos opcionales pasados al iniciar la aplicación
	 */
	public static void main(String[] args) {
		SpringApplication.run(P1SpringBootApplication.class, args);
	}

}
