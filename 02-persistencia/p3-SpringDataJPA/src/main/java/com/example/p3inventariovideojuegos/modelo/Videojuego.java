package com.example.p3inventariovideojuegos.modelo;

import java.math.BigDecimal;

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
 * Entidad JPA que representa un juego del inventario de la tienda.
 *
 * Hibernate relaciona esta clase con una fila de la tabla videojuegos.
 * El stock se guarda como dato de cada juego; todavía no hay relaciones
 * con categorías, plataformas u otros modelos.
 */
@Entity
@Table(name = "videojuegos")
@Getter
@Setter
@NoArgsConstructor
public class Videojuego {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "titulo", nullable = false, length = 120)
	private String titulo;

	@Column(name = "plataforma", nullable = false, length = 40)
	private String plataforma;

	@Column(name = "genero", nullable = false, length = 40)
	private String genero;

	@Column(name = "precio", nullable = false, precision = 8, scale = 2)
	private BigDecimal precio;

	@Column(name = "stock", nullable = false)
	private Integer stock;
}
