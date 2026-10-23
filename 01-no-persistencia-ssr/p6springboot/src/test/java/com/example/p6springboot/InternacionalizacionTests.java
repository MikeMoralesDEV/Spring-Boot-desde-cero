package com.example.p6springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class InternacionalizacionTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void usaEspanolComoIdiomaInicialYPermitePersonalizarElNombre() throws Exception {
		mockMvc.perform(get("/").param("nombre", "Lucía"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Una página, varios idiomas")))
				.andExpect(content().string(containsString("¡Bienvenido, Lucía!")))
				.andExpect(content().string(containsString("Idioma")));
	}

	@Test
	void cambiaALasTraduccionesInglesasCuandoSeSolicitaLangEn() throws Exception {
		mockMvc.perform(get("/").param("lang", "en").param("nombre", "Ada"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("One page, several languages")))
				.andExpect(content().string(containsString("Welcome, Ada!")))
				.andExpect(content().string(containsString("Language")));
	}

	@Test
	void mantieneElIdiomaElegidoDuranteLaSesion() throws Exception {
		MvcResult resultado = mockMvc.perform(get("/").param("lang", "en"))
				.andExpect(status().isOk())
				.andReturn();
		MockHttpSession sesion = (MockHttpSession) resultado.getRequest().getSession(false);

		assertTrue(sesion != null, "La selección de idioma debe crear una sesión.");
		mockMvc.perform(get("/").session(sesion))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("One page, several languages")));
	}

	@Test
	void permiteCambiarDeIdiomaVariasVecesEnLaMismaSesion() throws Exception {
		MvcResult resultadoIngles = mockMvc.perform(get("/").param("lang", "en"))
				.andExpect(status().isOk())
				.andReturn();
		MockHttpSession sesion = (MockHttpSession) resultadoIngles.getRequest().getSession(false);

		mockMvc.perform(get("/").session(sesion).param("lang", "es"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Una página, varios idiomas")))
				.andExpect(content().string(containsString("¡Bienvenido, Alex!")));

		mockMvc.perform(get("/").session(sesion).param("lang", "en"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("One page, several languages")))
				.andExpect(content().string(containsString("Welcome, Alex!")));
	}

	@Test
	void escapaElNombreRecibidoComoTextoEnLaVista() throws Exception {
		mockMvc.perform(get("/").param("nombre", "<script>alert(1)</script>"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(
						"&lt;script&gt;alert(1)&lt;/script&gt;")));
	}
}
