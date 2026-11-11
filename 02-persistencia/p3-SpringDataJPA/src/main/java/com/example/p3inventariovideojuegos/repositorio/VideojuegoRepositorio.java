package com.example.p3inventariovideojuegos.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.p3inventariovideojuegos.modelo.Videojuego;

/**
 * Puerta de acceso a los datos persistentes de Videojuego.
 *
 * Al extender JpaRepository se obtienen operaciones habituales (por ejemplo,
 * findAll y save) sin escribir a mano consultas JDBC ni una implementación.
 * Spring Data crea la implementación al arrancar la aplicación.
 */
public interface VideojuegoRepositorio extends JpaRepository<Videojuego, Long> {

	/**
	 * Ejemplo didáctico de consulta JPQL escrita por el equipo.
	 *
	 * Esta consulta solo ilustra cómo declarar una búsqueda propia. Para obtener
	 * todos los juegos no hace falta: JpaRepository ya ofrece findAll(), e incluso
	 * findAll(Sort) permite pedirlos ordenados sin escribir JPQL. En un proyecto
	 * real usaríamos el método heredado si cubre la necesidad.
	 */
	@Query("SELECT videojuego FROM Videojuego videojuego ORDER BY videojuego.titulo ASC")
	List<Videojuego> findAllOrdenadosPorTitulo();
}
