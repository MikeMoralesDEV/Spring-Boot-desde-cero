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

	// Opciones de @Column (Jakarta Persistence 3.2):
	// name: nombre de la columna; si se omite, se infiere del atributo Java.
	// unique: solicita una restricción de unicidad para esta columna.
	// nullable: indica si la columna permite NULL.
	// insertable / updatable: permite excluirla de los INSERT / UPDATE generados.
	// columnDefinition: fragmento DDL para definir el tipo y opciones de la columna.
	// options: texto DDL adicional que se añade a la definición de columna.
	// table: tabla a la que pertenece si el mapeo usa varias tablas.
	// length: longitud de columnas de texto; valor predeterminado: 255.
	// precision / scale: precisión total y dígitos decimales para números decimales.
	// secondPrecision: precisión fraccionaria de segundos para tipos temporales.
	// check: restricciones CHECK que se incluyen en el DDL generado.
	// comment: comentario descriptivo de la columna en el esquema, si se admite.
	// Estas opciones describen el mapeo/DDL; no todas validan datos en Java.
	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(nullable = false, length = 60)
	private String categoria;

	@Column(nullable = false, length = 300)
	private String descripcion;

	@Column(nullable = false)
	private boolean disponible;
}
