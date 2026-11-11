package com.example.p3inventariovideojuegos.controlador;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.p3inventariovideojuegos.modelo.Videojuego;
import com.example.p3inventariovideojuegos.servicio.VideojuegoServicio;
import com.example.p3inventariovideojuegos.dto.NuevoVideojuegoFormulario;

/**
 * Une las peticiones HTTP con los casos de uso y la plantilla Thymeleaf.
 *
 * No contiene consultas SQL ni reglas de persistencia: esas responsabilidades
 * corresponden al repositorio y al servicio.
 */
@Controller
public class VideojuegoControlador {

	private static final List<String> PLATAFORMAS = List.of(
			"PC", "PlayStation 5", "Xbox Series X|S", "Nintendo Switch", "Otra");
	private static final List<String> GENEROS = List.of(
			"Acción", "Aventura", "Deportes", "Estrategia", "Indie", "RPG",
			"Simulación", "Terror", "Otro");

	private final VideojuegoServicio videojuegoServicio;

	public VideojuegoControlador(VideojuegoServicio videojuegoServicio) {
		this.videojuegoServicio = videojuegoServicio;
	}

	/**
	 * GET muestra la página y recupera la lista completa del inventario.
	 * El formulario vacío permite añadir un juego en la misma página.
	 */
	@GetMapping("/")
	public String mostrarInventario(Model model) {
		prepararPagina(model, new NuevoVideojuegoFormulario());
		return "inventario";
	}

	/**
	 * POST recibe el formulario, valida sus campos en el servidor y delega
	 * el alta al servicio. Los errores vuelven a mostrarse junto a la lista.
	 */
	@PostMapping("/videojuegos")
	public String registrarVideojuego(
			@Valid @ModelAttribute("nuevoVideojuego") NuevoVideojuegoFormulario formulario,
			BindingResult resultado,
			Model model,
			RedirectAttributes redirectAttributes) {
		if (resultado.hasErrors()) {
			// Al volver a pintar la página hay que cargar otra vez todos los datos
			// que necesita la vista; no basta con conservar solo los campos del formulario.
			prepararPagina(model, formulario);
			return "inventario";
		}

		videojuegoServicio.registrar(
				formulario.getTitulo(),
				formulario.getPlataforma(),
				formulario.getGenero(),
				formulario.getPrecio(),
				formulario.getStock());

		// POST/Redirect/GET evita repetir el alta si se actualiza la página de respuesta.
		redirectAttributes.addFlashAttribute(
				"mensajeExito", "Juego añadido al inventario. ¡Que empiece la partida!");
		return "redirect:/";
	}

	/**
	 * Construye el modelo MVC común a la carga inicial y a la respuesta con errores.
	 * Model es el contenedor de datos que Thymeleaf utilizará para generar el HTML.
	 */
	private void prepararPagina(Model model, NuevoVideojuegoFormulario formulario) {
		List<Videojuego> videojuegos = videojuegoServicio.obtenerTodos();
		model.addAttribute("videojuegos", videojuegos);
		model.addAttribute("nuevoVideojuego", formulario);
		model.addAttribute("plataformas", PLATAFORMAS);
		model.addAttribute("generos", GENEROS);
	}
}
