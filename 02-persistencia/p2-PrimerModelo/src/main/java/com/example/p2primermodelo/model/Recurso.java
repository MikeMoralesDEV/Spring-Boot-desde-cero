package com.example.p2primermodelo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA que Hibernate mapea a una tabla y cuyas columnas se describen
 * mediante los atributos persistentes de esta clase.
 *
 * Lombok genera los getters, setters y el constructor sin argumentos que JPA
 * necesita. Se evitan @Data, equals y toString automáticos para no introducir
 * comportamiento de identidad inadecuado al ampliar el modelo con relaciones.
 */
@Entity
@Table(name = "recursos")
@Getter
@Setter
@NoArgsConstructor
public class Recurso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(nullable = false, length = 60)
	private String categoria;

	@Column(nullable = false, length = 300)
	private String descripcion;

	@Column(nullable = false)
	private boolean disponible;
}
