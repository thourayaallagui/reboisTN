package ms_inscriptions;

import ms_inscriptions.exception.GestionGlobaleExceptions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GestionGlobaleExceptionsTests {

	private final MockMvc mockMvc = MockMvcBuilders
			.standaloneSetup(new ControleurQuiEchoue())
			.setControllerAdvice(new GestionGlobaleExceptions())
			.build();

	@Test
	void exceptionInattendueRetourne500SansDetailTechnique() throws Exception {
		mockMvc.perform(get("/test/erreur-interne"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value(500))
				.andExpect(jsonPath("$.message").value("Une erreur interne est survenue."))
				.andExpect(jsonPath("$.message", not(containsString("détail interne"))));
	}

	@RestController
	static class ControleurQuiEchoue {

		@GetMapping("/test/erreur-interne")
		String declencherErreur() {
			throw new IllegalStateException("détail interne qui ne doit pas être exposé");
		}
	}
}
