package com.example.p4springboot;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Objeto temporal para enlazar campos de una petición web.
 *
 * No es una entidad ni se guarda en una base de datos: Spring crea y enlaza
 * una instancia para cada petición. Los getters y setters permiten el binding.
 */
public class SolicitudForm {

	@NotBlank(message = "Escribe tu nombre.")
	@Size(max = 60, message = "El nombre no puede superar los 60 caracteres.")
	private String nombre;

	@NotBlank(message = "Escribe una dirección de correo.")
	@Email(message = "Introduce una dirección de correo válida.")
	@Size(max = 120, message = "El correo no puede superar los 120 caracteres.")
	private String correo;

	@Pattern(regexp = "^$|^[0-9+() .-]{7,20}$",
			message = "El teléfono debe tener entre 7 y 20 caracteres válidos.")
	private String telefono;

	@NotBlank(message = "Selecciona un tema.")
	@Pattern(regexp = "CONSULTA|INCIDENCIA|SUGERENCIA",
			message = "Selecciona uno de los temas disponibles.")
	private String tema;

	@NotBlank(message = "Selecciona cómo prefieres que te contactemos.")
	@Pattern(regexp = "CORREO|TELEFONO",
			message = "Selecciona un canal de contacto válido.")
	private String canal;

	@NotBlank(message = "Escribe un mensaje.")
	@Size(min = 20, max = 1000,
			message = "El mensaje debe tener entre 20 y 1000 caracteres.")
	private String mensaje;

	@NotNull(message = "Selecciona una valoración.")
	@Min(value = 1, message = "La valoración mínima es 1.")
	@Max(value = 5, message = "La valoración máxima es 5.")
	private Integer valoracion;

	@AssertTrue(message = "Debes aceptar el aviso de privacidad para continuar.")
	private boolean aceptaPrivacidad;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Integer getValoracion() {
		return valoracion;
	}

	public void setValoracion(Integer valoracion) {
		this.valoracion = valoracion;
	}

	public boolean isAceptaPrivacidad() {
		return aceptaPrivacidad;
	}

	public void setAceptaPrivacidad(boolean aceptaPrivacidad) {
		this.aceptaPrivacidad = aceptaPrivacidad;
	}
}
