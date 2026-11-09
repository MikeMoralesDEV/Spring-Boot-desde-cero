package com.example.p2primermodelo;

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
class P2PrimerModeloApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void presentaLosObjetosDelModeloEnLaPagina() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Kit de microcontrolador")))
				.andExpect(content().string(containsString("Cámara de documentación")))
				.andExpect(content().string(containsString("Portátil de préstamo")))
				.andExpect(content().string(containsString("Objetos recibidos por la vista")));
	}
}
