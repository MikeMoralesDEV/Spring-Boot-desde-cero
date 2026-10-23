package com.example.p4springboot;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Recibe formularios de ejemplo, valida los datos y los presenta en otras vistas.
 * Ninguna solicitud se almacena de forma permanente.
 */
@Controller
public class FormularioController {

	private static final List<String> TEMAS = List.of(
			"CONSULTA", "INCIDENCIA", "SUGERENCIA");
	private static final Pattern CORREO_SIMPLE =
			Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

	/**
	 * Prepara la variante con DTO para un primer envío vacío.
	 *
	 * @param model modelo MVC con el objeto enlazado y las opciones de los controles
	 * @return nombre lógico de la vista del formulario con DTO
	 */
	@GetMapping("/")
	public String mostrarFormulario(Model model) {
		model.addAttribute("solicitud", new SolicitudForm());
		anadirOpciones(model);
		return "formulario";
	}

	/**
	 * Inicializa la variante que enlaza cada campo a un parámetro separado.
	 *
	 * @param model atributos sencillos para rellenar la vista
	 * @return nombre lógico de la vista del formulario sin DTO
	 */
	@GetMapping("/sin-dto")
	public String mostrarFormularioSinDto(Model model) {
		anadirValoresSimples(model, "", "", "");
		model.addAttribute("errores", Map.of());
		return "formulario-sin-dto";
	}

	/**
	 * Enlaza el POST con un DTO y ejecuta Jakarta Bean Validation.
	 *
	 * BindingResult debe aparecer inmediatamente después del objeto validado:
	 * contiene los errores para volver a renderizar el formulario con sus valores.
	 *
	 * @param solicitud valores recibidos y enlazados por Spring MVC
	 * @param resultadoBinding errores de conversión y validación del objeto
	 * @param model modelo que se necesita para reconstruir las opciones
	 * @return formulario con errores o vista de resultado
	 */
	@PostMapping("/enviar")
	public String recibirFormulario(
			@Valid @ModelAttribute("solicitud") SolicitudForm solicitud,
			BindingResult resultadoBinding,
			Model model) {
		anadirOpciones(model);

		// Regla entre dos campos: Bean Validation de propiedades no expresa esta relación.
		if ("TELEFONO".equals(solicitud.getCanal())
				&& (solicitud.getTelefono() == null || solicitud.getTelefono().isBlank())) {
			resultadoBinding.rejectValue(
					"telefono",
					"telefono.requerido",
					"Indica un teléfono si prefieres que te contactemos por teléfono.");
		}

		if (resultadoBinding.hasErrors()) {
			return "formulario";
		}

		// El mismo objeto de esta petición se añade implícitamente al ModelAttribute
		// y queda disponible para la vista resultado; no se guarda para otra petición.
		return "resultado";
	}

	/**
	 * Recibe tres campos escalares y aplica sus reglas manualmente, sin DTO.
	 *
	 * @param nombre nombre enviado
	 * @param correo correo enviado
	 * @param comentario texto del comentario enviado
	 * @param model errores o valores que mostrará la vista
	 * @return formulario con errores o resultado del envío válido
	 */
	@PostMapping("/enviar-sin-dto")
	public String recibirFormularioSinDto(
			@RequestParam(name = "nombre", defaultValue = "") String nombre,
			@RequestParam(name = "correo", defaultValue = "") String correo,
			@RequestParam(name = "comentario", defaultValue = "") String comentario,
			Model model) {
		String nombreNormalizado = nombre.trim();
		String correoNormalizado = correo.trim();
		String comentarioNormalizado = comentario.trim();
		Map<String, String> errores = new LinkedHashMap<>();

		if (nombreNormalizado.isEmpty()) {
			errores.put("nombre", "Escribe tu nombre.");
		} else if (nombreNormalizado.length() > 60) {
			errores.put("nombre", "El nombre no puede superar los 60 caracteres.");
		}

		if (!CORREO_SIMPLE.matcher(correoNormalizado).matches()) {
			errores.put("correo", "Introduce un correo con un formato básico válido.");
		} else if (correoNormalizado.length() > 120) {
			errores.put("correo", "El correo no puede superar los 120 caracteres.");
		}

		if (comentarioNormalizado.length() < 20 || comentarioNormalizado.length() > 500) {
			errores.put("comentario", "El comentario debe tener entre 20 y 500 caracteres.");
		}

		if (!errores.isEmpty()) {
			anadirValoresSimples(model, nombre, correo, comentario);
			model.addAttribute("errores", errores);
			return "formulario-sin-dto";
		}

		model.addAttribute("nombre", nombreNormalizado);
		model.addAttribute("correo", correoNormalizado);
		model.addAttribute("comentario", comentarioNormalizado);
		return "resultado-sin-dto";
	}

	/**
	 * Añade las opciones válidas para el selector y los radios del formulario DTO.
	 *
	 * @param model modelo MVC al que se añaden las listas de opciones
	 */
	private void anadirOpciones(Model model) {
		model.addAttribute("temasDisponibles", TEMAS);
		model.addAttribute("canalesDisponibles", List.of("CORREO", "TELEFONO"));
	}

	/**
	 * Repuebla los campos y mensajes simples cuando falla la variante sin DTO.
	 *
	 * @param model modelo MVC de la plantilla
	 * @param nombre nombre recibido
	 * @param correo correo recibido
	 * @param comentario comentario recibido
	 */
	private void anadirValoresSimples(Model model, String nombre, String correo, String comentario) {
		model.addAttribute("nombre", nombre);
		model.addAttribute("correo", correo);
		model.addAttribute("comentario", comentario);
	}
}
