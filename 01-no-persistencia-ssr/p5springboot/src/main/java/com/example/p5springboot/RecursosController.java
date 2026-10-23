package com.example.p5springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Rutas sencillas para dos páginas que reutilizan fragmentos Thymeleaf.
 */
@Controller
public class RecursosController {

	/**
	 * Muestra el catálogo de recursos con cabecera, navegación y pie reutilizados.
	 *
	 * @return nombre lógico de la vista principal
	 */
	@GetMapping("/")
	public String inicio() {
		return "inicio";
	}

	/**
	 * Muestra una segunda vista con los mismos fragmentos comunes.
	 *
	 * @return nombre lógico de la vista de detalle
	 */
	@GetMapping("/detalle")
	public String detalle() {
		return "detalle";
	}
}
