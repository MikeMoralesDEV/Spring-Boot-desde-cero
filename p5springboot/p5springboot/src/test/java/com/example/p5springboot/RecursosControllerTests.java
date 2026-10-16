package com.example.p5springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RecursosControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void catalogoRenderizaImagenNavegacionYFragmentos() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("href=\"/css/p5.css\"")))
				.andExpect(content().string(containsString("src=\"/images/chikapu.jpg\"")))
				.andExpect(content().string(containsString("alt=\"Peluche amarillo sentado")))
				.andExpect(content().string(containsString("Este contenedor permanece alrededor del fragmento.")))
				.andExpect(content().string(containsString(
						"href=\"https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.html\"")))
				.andExpect(content().string(containsString("WebP y AVIF")))
				.andExpect(result -> assertEquals(2, countOccurrences(
						result.getResponse().getContentAsString(), "P5 · Los fragmentos")));
	}

	@Test
	void vistaDetalleReutilizaFragmentosConOtrosParametros() throws Exception {
		mockMvc.perform(get("/detalle"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Detalle del recurso")))
				.andExpect(content().string(containsString("aria-current=\"page\"")))
				.andExpect(content().string(containsString("La leyenda contextualiza la imagen")));
	}

	@Test
	void imagenLocalSeSirveComoRecursoEstatico() throws Exception {
		mockMvc.perform(get("/images/chikapu.jpg"))
				.andExpect(status().isOk())
				.andExpect(result -> assertEquals("image/jpeg",
						result.getResponse().getContentType()));
	}

	private int countOccurrences(String text, String fragment) {
		int count = 0;
		int offset = 0;
		while ((offset = text.indexOf(fragment, offset)) >= 0) {
			count++;
			offset += fragment.length();
		}
		return count;
	}
}
