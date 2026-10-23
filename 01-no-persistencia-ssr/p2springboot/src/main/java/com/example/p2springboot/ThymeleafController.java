package com.example.p2springboot;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Prepara valores sencillos para practicar cómo Thymeleaf genera HTML dinámico.
 * No se necesitan DTO ni clases propias para pasar cadenas, números y listas.
 */
@Controller
public class ThymeleafController {

	@GetMapping("/")
	public String mostrarEjemplos(
			@RequestParam(name = "alumno", defaultValue = "estudiante") String alumno,
			Model model) {
		// Model es el contenedor de Spring MVC que hace visibles estos valores
		// en la plantilla; aquí solo añadimos tipos básicos y listas de Java.
		model.addAttribute("alumno", alumno);
		model.addAttribute("modulo", "Thymeleaf");
		model.addAttribute("numeroLeccion", 2);
		model.addAttribute("hayClase", false);
		model.addAttribute("temas", List.of(
				"Mostrar texto con th:text",
				"Evaluar condiciones con th:if",
				"Recorrer listas con th:each",
				"Usar expresiones y utilidades"));
		model.addAttribute("duraciones", List.of(15, 25, 40, 55));

		// El nombre devuelto se resuelve como templates/thymeleaf-laboratorio.html.
		return "thymeleaf-laboratorio";
	}
}
