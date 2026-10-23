package com.example.p1springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador web para las páginas relacionadas con los saludos.
 *
 * Las rutas de una aplicación se suelen agrupar por responsabilidad:
 * como la página de bienvenida y el saludo personalizado pertenecen a la
 * misma función, tiene sentido gestionarlos desde este controlador. Así se
 * mantienen juntas rutas relacionadas sin concentrar en una sola clase
 * funciones que no tienen relación entre sí.
 */
@Controller
public class SaludoController {

	/**
	 * Atiende las peticiones HTTP GET dirigidas a la ruta raíz ({@code /}).
	 *
	 * Devuelve la plantilla {@code hola-mundo.html}, ubicada en
	 * {@code src/main/resources/templates}. Thymeleaf la procesa y Spring MVC
	 * entrega el HTML resultante al navegador.
	 *
	 * @return el nombre lógico de la vista de bienvenida, sin extensión
	 */
	@GetMapping("/")
	public String mostrarHolaMundo() {
		return "hola-mundo";
	}

	/**
	 * Atiende peticiones GET como {@code /saludo?user=Miguel}.
	 *
	 * Spring obtiene el valor de {@code user} de los parámetros de la URL y
	 * lo añade al modelo, que es el conjunto de datos disponible para la vista.
	 * Si no se proporciona el parámetro, se utiliza "mundo" como valor por
	 * defecto. Thymeleaf mostrará el texto escapado en la página.
	 *
	 * @param user nombre que se incluirá en el saludo
	 * @param model modelo usado para pasar datos a la plantilla
	 * @return nombre lógico de la plantilla, sin extensión
	 */
	@GetMapping("/saludo")
	public String mostrarSaludo(
			@RequestParam(name = "user", defaultValue = "mundo") String user,
			Model model) {
		model.addAttribute("user", user);
		return "saludo";
	}
}
