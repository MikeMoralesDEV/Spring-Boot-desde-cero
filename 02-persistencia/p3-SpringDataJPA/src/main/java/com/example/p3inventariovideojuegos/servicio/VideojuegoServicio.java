package com.example.p3inventariovideojuegos.servicio;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.p3inventariovideojuegos.modelo.Videojuego;
import com.example.p3inventariovideojuegos.repositorio.VideojuegoRepositorio;

/**
 * Coordina los casos de uso relacionados con el inventario.
 *
 * El controlador se ocupa del protocolo web y la vista de presentar HTML;
 * este servicio expresa las operaciones de la aplicación sin depender
 * de una petición HTTP ni de una plantilla Thymeleaf.
 *
 * A primera vista esta capa puede parecer redundante: el repositorio ya ofrece
 * métodos para consultar y guardar entidades, y algunos métodos del servicio
 * simplemente los coordinan. En esta práctica mantenemos ambas capas para
 * enseñar sus responsabilidades distintas: el repositorio sabe acceder a los
 * datos; el servicio expresa casos de uso y reúne sus reglas y transacciones.
 * Por ejemplo, registrar construye la entidad, normaliza el título y ejecuta
 * la escritura dentro de una transacción.
 *
 * El servicio no es obligatorio por norma en toda aplicación pequeña. Si solo
 * se necesita una operación trivial y no hay reglas que coordinar, añadirlo
 * puede aportar poco. Se vuelve útil cuando el caso de uso combina operaciones,
 * aplica reglas de negocio o debe mantener varias escrituras en una transacción.
 *
 * @Transactional en la clase establece por defecto una transacción de solo lectura
 * para sus métodos públicos. Spring abre y cierra esa transacción alrededor de la
 * llamada al método mediante su infraestructura de proxies. No significa que cada
 * línea ejecute un COMMIT ni que readOnly impida por sí solo cualquier escritura:
 * es una indicación para el proveedor y puede ayudar a optimizar las consultas.
 */
@Service
@Transactional(readOnly = true)
public class VideojuegoServicio {

	private final VideojuegoRepositorio videojuegoRepositorio;

	/**
	 * Spring inyecta el repositorio por constructor. La dependencia explícita
	 * hace visible qué necesita el servicio para trabajar.
	 */
	public VideojuegoServicio(VideojuegoRepositorio videojuegoRepositorio) {
		this.videojuegoRepositorio = videojuegoRepositorio;
	}

	/**
	 * Recupera todos los juegos del inventario ordenados por título.
	 *
	 * Hereda la transacción readOnly de la clase: la lectura se ejecuta dentro
	 * de un contexto transaccional coherente, y al terminar Spring cierra la
	 * transacción. La anotación readOnly expresa la intención de no modificar;
	 * no sustituye permisos ni controles de integridad de la base de datos.
	 */
	public List<Videojuego> obtenerTodos() {
		return videojuegoRepositorio.findAllOrdenadosPorTitulo();
	}

	/**
	 * Construye y guarda un juego con los datos que el controlador ya ha validado.
	 *
	 * El @Transactional de este método sustituye el readOnly de la clase:
	 * esta operación sí modifica datos. Spring confirma la transacción si todo
	 * termina correctamente y la revierte ante una excepción no comprobada.
	 * De este modo, la escritura queda delimitada en el caso de uso del servicio,
	 * en lugar de depender de que el controlador conozca detalles de persistencia.
	 *
	 * @return entidad persistida, ahora con el identificador generado por MariaDB
	 */
	@Transactional
	public Videojuego registrar(
			String titulo,
			String plataforma,
			String genero,
			BigDecimal precio,
			Integer stock) {
		Videojuego videojuego = new Videojuego();
		videojuego.setTitulo(titulo.trim());
		videojuego.setPlataforma(plataforma);
		videojuego.setGenero(genero);
		videojuego.setPrecio(precio);
		videojuego.setStock(stock);

		// save delega en Spring Data JPA; Hibernate traduce la entidad a SQL INSERT.
		return videojuegoRepositorio.save(videojuego);
	}
}
