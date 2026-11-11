package com.example.p3inventariovideojuegos.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objeto de entrada del formulario HTTP para dar de alta un videojuego.
 *
 * No es una entidad JPA: representa únicamente los campos que el navegador
 * puede enviar. Al no contener el id, el cliente no puede escoger la clave
 * primaria que generará la base de datos.
 */
@Getter
@Setter
@NoArgsConstructor
public class NuevoVideojuegoFormulario {

	@NotBlank(message = "Escribe el título del juego.")
	@Size(max = 120, message = "El título no puede superar los 120 caracteres.")
	private String titulo;

	@NotBlank(message = "Selecciona una plataforma.")
	@Pattern(regexp = "PC|PlayStation 5|Xbox Series X\\|S|Nintendo Switch|Otra",
			message = "Selecciona una de las plataformas disponibles.")
	private String plataforma;

	@NotBlank(message = "Selecciona un género.")
	@Pattern(regexp = "Acción|Aventura|Deportes|Estrategia|Indie|RPG|Simulación|Terror|Otro",
			message = "Selecciona uno de los géneros disponibles.")
	private String genero;

	@NotNull(message = "Indica el precio.")
	@DecimalMin(value = "0.00", message = "El precio no puede ser negativo.")
	@Digits(integer = 6, fraction = 2, message = "Usa hasta 6 cifras enteras y 2 decimales.")
	private BigDecimal precio;

	@NotNull(message = "Indica cuántas unidades hay.")
	@Min(value = 0, message = "El stock no puede ser negativo.")
	@Max(value = 9999, message = "El stock no puede superar las 9999 unidades.")
	private Integer stock;
}
