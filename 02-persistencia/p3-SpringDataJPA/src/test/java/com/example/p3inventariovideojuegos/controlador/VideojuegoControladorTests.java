package com.example.p3inventariovideojuegos.controlador;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.p3inventariovideojuegos.modelo.Videojuego;
import com.example.p3inventariovideojuegos.servicio.VideojuegoServicio;
import com.example.p3inventariovideojuegos.dto.NuevoVideojuegoFormulario;
import com.example.p3inventariovideojuegos.web.dto.NuevoVideojuegoFormulario;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Pruebas de la capa web con MockMvc.
 *
 * @WebMvcTest carga el controlador y el renderizado MVC, no conecta a MariaDB.
 * El servicio se sustituye por un mock para concentrar estas pruebas en HTTP,
 * binding, validación y la plantilla.
 */
@WebMvcTest(VideojuegoControlador.class)
class VideojuegoControladorTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private VideojuegoServicio videojuegoServicio;

	@Test
	void getMuestraTodosLosJuegosYElFormularioDebajoDelListado() throws Exception {
		Videojuego juego = juegoDeEjemplo();
		when(videojuegoServicio.obtenerTodos()).thenReturn(List.of(juego));

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("inventario"))
				.andExpect(content().string(containsString("The Example Quest")))
				.andExpect(content().string(containsString("Guardar en inventario")))
				.andExpect(content().string(containsString("CHECKPOINT")));

		verify(videojuegoServicio).obtenerTodos();
	}

	@Test
	void postValidoRegistraElJuegoYRedirigeAlListado() throws Exception {
		mockMvc.perform(post("/videojuegos")
						.param("titulo", "The Example Quest")
						.param("plataforma", "PC")
						.param("genero", "Aventura")
						.param("precio", "29.90")
						.param("stock", "4"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));

		verify(videojuegoServicio).registrar(
				"The Example Quest",
				"PC",
				"Aventura",
				new BigDecimal("29.90"),
				4);
	}

	@Test
	void postInvalidoNoRegistraYVuelveAMostrarElFormularioConErrores() throws Exception {
		when(videojuegoServicio.obtenerTodos()).thenReturn(List.of());

		mockMvc.perform(post("/videojuegos")
						.param("titulo", "")
						.param("plataforma", "PC")
						.param("genero", "Aventura")
						.param("precio", "-1")
						.param("stock", "-2"))
				.andExpect(status().isOk())
				.andExpect(view().name("inventario"))
				.andExpect(content().string(containsString("Escribe el título del juego.")))
				.andExpect(content().string(containsString("El precio no puede ser negativo.")))
				.andExpect(content().string(containsString("El stock no puede ser negativo.")));
	}

	private Videojuego juegoDeEjemplo() {
		Videojuego juego = new Videojuego();
		juego.setId(1L);
		juego.setTitulo("The Example Quest");
		juego.setPlataforma("PC");
		juego.setGenero("Aventura");
		juego.setPrecio(new BigDecimal("29.90"));
		juego.setStock(4);
		return juego;
	}
}
