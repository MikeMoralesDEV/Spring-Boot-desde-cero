package com.example.p4springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class P4SpringBootApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void muestraFormularioConEnlacesYCamposThymeleaf() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("action=\"/enviar\"")))
				.andExpect(content().string(containsString("name=\"nombre\"")))
				.andExpect(content().string(containsString("name=\"aceptaPrivacidad\"")))
				.andExpect(content().string(containsString("Qué valida cada capa")))
				.andExpect(result -> assertEquals(1, countOccurrences(
						result.getResponse().getContentAsString(), "href=\"/sin-dto\"")));
	}

	@Test
	void procesaFormularioValidoYMuestraDatosEnOtraVista() throws Exception {
		mockMvc.perform(post("/enviar")
						.param("nombre", "Ana")
						.param("correo", "ana@example.com")
						.param("telefono", "")
						.param("tema", "CONSULTA")
						.param("canal", "CORREO")
						.param("mensaje", "Quiero recibir información detallada.")
						.param("valoracion", "5")
						.param("aceptaPrivacidad", "true"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Datos validados correctamente")))
				.andExpect(content().string(containsString("ana@example.com")))
				.andExpect(content().string(containsString("Quiero recibir información detallada.")));
	}

	@Test
	void validaEnServidorYVuelveAlFormularioConErrores() throws Exception {
		mockMvc.perform(post("/enviar")
						.param("nombre", "")
						.param("correo", "no-es-correo")
						.param("telefono", "")
						.param("tema", "INVALIDO")
						.param("canal", "TELEFONO")
						.param("mensaje", "corto")
						.param("valoracion", "9")
						.param("aceptaPrivacidad", "false"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Escribe tu nombre.")))
				.andExpect(content().string(containsString("Introduce una dirección de correo válida.")))
				.andExpect(content().string(containsString("Indica un teléfono si prefieres que te contactemos por teléfono.")))
				.andExpect(content().string(containsString("Debes aceptar el aviso de privacidad para continuar.")));
	}

	@Test
	void escapaValoresDelFormularioEnLaVistaDeResultado() throws Exception {
		mockMvc.perform(post("/enviar")
						.param("nombre", "Ana")
						.param("correo", "ana@example.com")
						.param("telefono", "")
						.param("tema", "CONSULTA")
						.param("canal", "CORREO")
						.param("mensaje", "<script>alert('x')</script> Este es un mensaje válido.")
						.param("valoracion", "4")
						.param("aceptaPrivacidad", "true"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("&lt;script&gt;alert(&#39;x&#39;)&lt;/script&gt;")));
	}

	@Test
	void formularioSinDtoUsaParametrosEscalaresYValidaEnServidor() throws Exception {
		mockMvc.perform(get("/sin-dto"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("name=\"nombre\"")))
				.andExpect(content().string(containsString("name=\"comentario\"")))
				.andExpect(content().string(containsString("@RequestParam")));

		mockMvc.perform(post("/enviar-sin-dto")
						.param("nombre", "Luis")
						.param("correo", "luis@example.com")
						.param("comentario", "Este comentario cumple el mínimo."))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Consulta recibida")))
				.andExpect(content().string(containsString("luis@example.com")));

		mockMvc.perform(post("/enviar-sin-dto")
						.param("nombre", "")
						.param("correo", "no-es-correo")
						.param("comentario", "corto"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Escribe tu nombre.")))
				.andExpect(content().string(containsString("Introduce un correo con un formato básico válido.")))
				.andExpect(content().string(containsString("El comentario debe tener entre 20 y 500 caracteres.")));
	}

	@Test
	void formularioSinDtoEscapaValoresRepoblados() throws Exception {
		mockMvc.perform(post("/enviar-sin-dto")
						.param("nombre", "<script>alert(1)</script>")
						.param("correo", "incorrecto")
						.param("comentario", "demasiado corto"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("&lt;script&gt;alert(1)&lt;/script&gt;")));
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
