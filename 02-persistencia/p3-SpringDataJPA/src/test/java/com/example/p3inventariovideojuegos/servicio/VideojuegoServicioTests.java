package com.example.p3inventariovideojuegos.servicio;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.p3inventariovideojuegos.modelo.Videojuego;
import com.example.p3inventariovideojuegos.repositorio.VideojuegoRepositorio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias del servicio.
 *
 * Mockito sustituye el repositorio por un doble de prueba: verificamos la lógica
 * de coordinación del servicio sin necesitar todavía un servidor MariaDB.
 */
@ExtendWith(MockitoExtension.class)
class VideojuegoServicioTests {

	@Mock
	private VideojuegoRepositorio videojuegoRepositorio;

	@InjectMocks
	private VideojuegoServicio videojuegoServicio;

	@Test
	void obtenerTodosSolicitaLaListaOrdenadaPorTitulo() {
		List<Videojuego> esperados = List.of(new Videojuego(), new Videojuego());
		when(videojuegoRepositorio.findAllOrdenadosPorTitulo())
				.thenReturn(esperados);

		List<Videojuego> resultado = videojuegoServicio.obtenerTodos();

		assertSame(esperados, resultado);
		verify(videojuegoRepositorio).findAllOrdenadosPorTitulo();
	}

	@Test
	void registrarConstruyeYEntregaLaEntidadAlRepositorio() {
		Videojuego persistido = new Videojuego();
		persistido.setId(42L);
		when(videojuegoRepositorio.save(org.mockito.ArgumentMatchers.any(Videojuego.class)))
				.thenReturn(persistido);

		Videojuego resultado = videojuegoServicio.registrar(
				"  Juego de prueba  ",
				"PC",
				"Aventura",
				new BigDecimal("24.90"),
				3);

		ArgumentCaptor<Videojuego> captor = ArgumentCaptor.forClass(Videojuego.class);
		verify(videojuegoRepositorio).save(captor.capture());
		assertEquals("Juego de prueba", captor.getValue().getTitulo());
		assertEquals("PC", captor.getValue().getPlataforma());
		assertEquals("Aventura", captor.getValue().getGenero());
		assertEquals(new BigDecimal("24.90"), captor.getValue().getPrecio());
		assertEquals(3, captor.getValue().getStock());
		assertSame(persistido, resultado);
	}
}
