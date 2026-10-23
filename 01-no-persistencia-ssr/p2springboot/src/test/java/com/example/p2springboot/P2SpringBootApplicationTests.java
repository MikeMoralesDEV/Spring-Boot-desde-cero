package com.example.p2springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class P2SpringBootApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void renderizaVariablesYRecorreLasListas() throws Exception {
		mockMvc.perform(get("/").param("alumno", "Ana"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("¡Bienvenido, Ana!")))
				.andExpect(content().string(containsString("Recorrer listas con th:each")))
				.andExpect(content().string(containsString("55")))
				.andExpect(content().string(containsString("Crear una secuencia sin prepararla en Java")));
	}

	@Test
	void escapaElTextoRecibidoEnLaUrl() throws Exception {
		mockMvc.perform(get("/").param("alumno", "<script>alert(1)</script>"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("&lt;script&gt;alert(1)&lt;/script&gt;")));
	}
}
