package com.example.p3springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class P3SpringBootApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void paginaPrincipalGeneraEnlacesYFormulario() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("href=\"/css/p3.css\"")))
				.andExpect(content().string(containsString("/saludo?nombre=")))
				.andExpect(content().string(containsString("/alumnos/42")));
	}

	@Test
	void recibeParametroDeConsultaYVariableDeRuta() throws Exception {
		mockMvc.perform(get("/saludo").param("nombre", "Lucía"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("¡Hola, Lucía!")));

		mockMvc.perform(get("/alumnos/42").param("vista", "contacto"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Identificador de alumno:")))
				.andExpect(content().string(containsString("contacto")));
	}

	@Test
	void endpointsDeDemostracionDevuelvenEstadosDeError() throws Exception {
		mockMvc.perform(get("/demo/error/400")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/demo/error/403")).andExpect(status().isForbidden());
		mockMvc.perform(get("/demo/error/404")).andExpect(status().isNotFound());
		mockMvc.perform(get("/demo/error/500")).andExpect(status().isInternalServerError());
	}
}
