package ms_suivi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MsSuiviApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void suiviCrudPersistsAndCascadesChildren() throws Exception {
		String payload = """
				{
				  "arbreId": 99,
				  "dateSuivi": "2024-07-01",
				  "hauteur": 1.5,
				  "diametreTronc": 10.0,
				  "etatSante": "BON",
				  "observation": "Test POST",
				  "interventions": [
				    { "typeIntervention": "ARROSAGE", "dateIntervention": "2024-07-01", "description": "Test" }
				  ],
				  "photos": [
				    { "urlPhoto": "http://example.com/photos/test.jpg", "dateAjout": "2024-07-01" }
				  ]
				}
				""";

		MvcResult postResult = mockMvc.perform(post("/api/suivis")
					.with(httpBasic("test-user", "test-password"))
					.contentType(MediaType.APPLICATION_JSON)
					.content(payload))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.interventions.length()").value(1))
				.andExpect(jsonPath("$.photos.length()").value(1))
				.andReturn();

		JsonNode created = objectMapper.readTree(postResult.getResponse().getContentAsString());
		long id = created.get("id").asLong();

		mockMvc.perform(get("/api/suivis/{id}", id)
					.with(httpBasic("test-user", "test-password")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.interventions.length()").value(1))
				.andExpect(jsonPath("$.photos.length()").value(1));

		String updatePayload = payload.replace("Test POST", "Test PUT");
		mockMvc.perform(put("/api/suivis/{id}", id)
					.with(httpBasic("test-user", "test-password"))
					.contentType(MediaType.APPLICATION_JSON)
					.content(updatePayload))
				.andExpect(status().isOk());

		mockMvc.perform(get("/api/suivis/{id}", id)
					.with(httpBasic("test-user", "test-password")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.observation").value("Test PUT"))
				.andExpect(jsonPath("$.interventions.length()").value(1))
				.andExpect(jsonPath("$.photos.length()").value(1));

		mockMvc.perform(delete("/api/suivis/{id}", id)
					.with(httpBasic("test-user", "test-password")))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/suivis/{id}", id)
					.with(httpBasic("test-user", "test-password")))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Not Found"));
	}

	@Test
	void gatewayCompatibleAliasesAndHelloRequireAuthentication() throws Exception {
		mockMvc.perform(get("/hello"))
				.andExpect(status().isUnauthorized());

		mockMvc.perform(get("/api/suivis/interventions")
					.with(httpBasic("test-user", "test-password")))
				.andExpect(status().isOk());

		mockMvc.perform(get("/api/suivis/photos")
					.with(httpBasic("test-user", "test-password")))
				.andExpect(status().isOk());

		mockMvc.perform(get("/hello")
					.with(httpBasic("test-user", "test-password")))
				.andExpect(status().isOk());
	}

	@Test
	void invalidInputIsBadRequestAndMissingResourcesAreNotFound() throws Exception {
		mockMvc.perform(get("/api/suivis/arbre/1/date/not-a-date")
					.with(httpBasic("test-user", "test-password")))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Bad Request"));

		mockMvc.perform(post("/api/interventions")
					.with(httpBasic("test-user", "test-password"))
					.contentType(MediaType.APPLICATION_JSON)
					.content("{}"))
				.andExpect(status().isBadRequest());

		mockMvc.perform(get("/api/interventions/999999")
					.with(httpBasic("test-user", "test-password")))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Not Found"));

		mockMvc.perform(get("/api/photos/999999")
					.with(httpBasic("test-user", "test-password")))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Not Found"));
	}

}
