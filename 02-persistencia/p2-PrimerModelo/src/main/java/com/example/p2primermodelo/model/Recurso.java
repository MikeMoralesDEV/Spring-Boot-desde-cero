package com.example.p2primermodelo.model;

/**
 * Representa un recurso del aula maker como un objeto Java.
 *
 * Por ahora es una clase sencilla: sus objetos solo viven en memoria y no se
 * guardan en una base de datos. Más adelante se estudiará qué cambios hacen
 * falta para mapear una clase como esta a una tabla.
 */
public class Recurso {

	// Es un identificador de ejemplo en memoria, todavía no una clave primaria de base de datos.
	private Long id;
	private String nombre;
	private String categoria;
	private String descripcion;
	private boolean disponible;

	/**
	 * Constructor sin argumentos para poder crear primero un objeto vacío
	 * y asignar después sus atributos mediante los setters.
	 */
	public Recurso() {
	}

	/**
	 * Crea un objeto Recurso con todos sus datos de ejemplo.
	 *
	 * @param id identificador numérico usado solo dentro de este ejemplo
	 * @param nombre nombre que se mostrará en el catálogo
	 * @param categoria grupo al que pertenece el recurso
	 * @param descripcion explicación breve del recurso
	 * @param disponible indica si el recurso está disponible
	 */
	public Recurso(Long id, String nombre, String categoria, String descripcion, boolean disponible) {
		this.id = id;
		this.nombre = nombre;
		this.categoria = categoria;
		this.descripcion = descripcion;
		this.disponible = disponible;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
}
