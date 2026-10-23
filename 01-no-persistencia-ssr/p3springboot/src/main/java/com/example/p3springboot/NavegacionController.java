package com.example.p3springboot;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

/**
 * Rutas didácticas para practicar enlaces Thymeleaf, parámetros HTTP,
 * variables de ruta, formularios y respuestas de error sin persistencia.
 */
@Controller
public class NavegacionController {

	/**
	 * Página principal con ejemplos para los enlaces construidos en Thymeleaf.
	 *
	 * @param model valores de ejemplo que la plantilla puede usar al construir URLs
	 * @return nombre lógico de la vista principal
	 */
	@GetMapping("/")
	public String inicio(Model model) {
		model.addAttribute("nombreEjemplo", "Lucía");
		model.addAttribute("idEjemplo", 42);
		return "inicio";
	}

	/**
	 * Lee un parámetro de consulta. Thymeleaf genera el enlace con
	 * {@code @{/saludo(nombre='Lucía')}} y Spring lo recibe con @RequestParam.
	 *
	 * @param nombre valor de la consulta o valor predeterminado
	 * @param model datos para la plantilla de respuesta
	 * @return nombre lógico de la vista de saludo
	 */
	@GetMapping("/saludo")
	public String saludo(
			@RequestParam(name = "nombre", defaultValue = "estudiante") String nombre,
			Model model) {
		model.addAttribute("nombre", nombre);
		return "saludo";
	}

	/**
	 * Lee el identificador que forma parte de la ruta, no de la query string.
	 * El ejemplo es solo demostrativo y no consulta ninguna base de datos.
	 *
	 * @param id identificador incluido en la ruta, por ejemplo /alumnos/42
	 * @param vista parámetro de consulta opcional que acompaña a la ruta
	 * @param model datos sencillos para la vista
	 * @return nombre lógico de la vista del alumno
	 */
	@GetMapping("/alumnos/{id}")
	public String alumno(
			@PathVariable long id,
			@RequestParam(name = "vista", defaultValue = "resumen") String vista,
			Model model) {
		model.addAttribute("id", id);
		model.addAttribute("vista", vista);
		return "alumno";
	}

	/**
	 * Provoca un 400 para mostrar el manejo de errores de cliente.
	 *
	 * @throws ResponseStatusException respuesta HTTP 400 intencionada
	 */
	@GetMapping("/demo/error/400")
	public void errorDePeticion() {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error de demostración");
	}

	/**
	 * Provoca un 403 para mostrar el manejo de errores de cliente.
	 *
	 * @throws ResponseStatusException respuesta HTTP 403 intencionada
	 */
	@GetMapping("/demo/error/403")
	public void accesoProhibido() {
		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Error de demostración");
	}

	/**
	 * Provoca un 404 mediante una ruta existente; la página es gestionada
	 * por el mecanismo de errores de Spring Boot.
	 *
	 * @throws ResponseStatusException respuesta HTTP 404 intencionada
	 */
	@GetMapping("/demo/error/404")
	public void noEncontrado() {
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error de demostración");
	}

	/**
	 * Provoca un 500 para demostrar la plantilla general de errores de servidor.
	 *
	 * @throws ResponseStatusException respuesta HTTP 500 intencionada
	 */
	@GetMapping("/demo/error/500")
	public void errorInterno() {
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error de demostración");
	}
}
