package com.example.p6springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Muestra una página de ejemplo cuyo contenido visible se localiza con mensajes.
 */
@Controller
public class InicioController {

	/**
	 * Presenta la página de bienvenida.
	 *
	 * El idioma no se lee aquí: LocaleChangeInterceptor lo actualiza antes de que
	 * Spring llegue al controlador, y Thymeleaf utiliza ese Locale al resolver #{...}.
	 *
	 * @param nombre nombre opcional para mostrar un ejemplo de mensaje con parámetro
	 * @param model datos dinámicos que la plantilla necesita
	 * @return nombre lógico de la plantilla Thymeleaf
	 */
	@GetMapping("/")
	public String inicio(
			@RequestParam(name = "nombre", defaultValue = "Alex") String nombre,
			Model model) {
		model.addAttribute("nombre", nombre);
		return "inicio";
	}
}
