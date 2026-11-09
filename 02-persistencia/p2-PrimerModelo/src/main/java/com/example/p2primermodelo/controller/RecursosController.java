package com.example.p2primermodelo.controller;

import java.util.List;

import com.example.p2primermodelo.model.Recurso;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Prepara los objetos Recurso que Thymeleaf presenta en el catálogo.
 *
 * La lista es fija y está en memoria: este ejemplo no utiliza base de datos.
 */
@Controller
public class RecursosController {

	private static final List<Recurso> RECURSOS = List.of(
			new Recurso(1L, "Kit de microcontrolador", "Electrónica",
					"Placa de desarrollo con cable USB para prácticas de electrónica.", true),
			new Recurso(2L, "Cámara de documentación", "Imagen",
					"Cámara con soporte de mesa para mostrar documentos y prototipos.", false),
			new Recurso(3L, "Portátil de préstamo", "Informática",
					"Ordenador portátil con cargador y funda.", true));

	/**
	 * Pasa una colección de objetos al Model de Spring MVC.
	 *
	 * El Model es el contenedor que Spring entrega a la plantilla; cada elemento
	 * de la colección, en cambio, es un objeto de nuestra clase Recurso.
	 *
	 * @param model contenedor de atributos disponibles en la vista
	 * @return nombre lógico de la plantilla del catálogo
	 */
	@GetMapping("/")
	public String mostrarRecursos(Model model) {
		model.addAttribute("recursos", RECURSOS);
		return "recursos";
	}
}
