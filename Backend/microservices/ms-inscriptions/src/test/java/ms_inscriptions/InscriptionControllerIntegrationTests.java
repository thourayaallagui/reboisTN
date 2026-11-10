package ms_inscriptions;

import ms_inscriptions.dto.InscriptionRequest;
import ms_inscriptions.repository.InscriptionRepository;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InscriptionControllerIntegrationTests {

	private static final String BASE_URL = "/api/inscriptions";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private InscriptionRepository inscriptionRepository;

	@BeforeEach
	void nettoyerInscriptions() {
		inscriptionRepository.deleteAll();
	}

	@Test
	void creerInscriptionRetourne201EtStatutEnAttente() throws Exception {
		mockMvc.perform(post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(corps(101L, 201L)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.statut").value("EN_ATTENTE"))
				.andExpect(jsonPath("$.presence").value(false));
	}

	@Test
	void consulterLaListeRetourne200() throws Exception {
		creerInscription(102L, 202L);

		mockMvc.perform(get(BASE_URL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void consulterParIdRetourne200() throws Exception {
		long id = creerInscription(103L, 203L);

		mockMvc.perform(get(BASE_URL + "/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value((int) id))
				.andExpect(jsonPath("$.identifiantBenevole").value(103));
	}

	@Test
	void consulterParBenevoleEtEvenementRetourne200() throws Exception {
		long id = creerInscription(104L, 204L);

		mockMvc.perform(get(BASE_URL + "/benevole/104"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value((int) id));

		mockMvc.perform(get(BASE_URL + "/evenement/204"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value((int) id));
	}

	@Test
	void doublonBenevoleEvenementRetourne409() throws Exception {
		creerInscription(105L, 205L);

		mockMvc.perform(post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(corps(105L, 205L)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409))
				.andExpect(jsonPath("$.message").isNotEmpty());
	}

	@Test
	void inscriptionInexistanteRetourne404() throws Exception {
		mockMvc.perform(get(BASE_URL + "/999999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.timestamp").isNotEmpty());
	}

	@Test
	void confirmerInscriptionRetourne200() throws Exception {
		long id = creerInscription(106L, 206L);

		mockMvc.perform(patch(BASE_URL + "/" + id + "/confirmer"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.statut").value("CONFIRMEE"));
	}

	@Test
	void annulerInscriptionRetourne200() throws Exception {
		long id = creerInscription(107L, 207L);

		mockMvc.perform(patch(BASE_URL + "/" + id + "/annuler"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.statut").value("ANNULEE"));
	}

	@Test
	void transitionDepuisUnStatutAnnuleRetourne409() throws Exception {
		long id = creerInscription(108L, 208L);
		mockMvc.perform(patch(BASE_URL + "/" + id + "/annuler"))
				.andExpect(status().isOk());

		mockMvc.perform(patch(BASE_URL + "/" + id + "/confirmer"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409));
	}

	@Test
	void enregistrerPresencePourInscriptionConfirmeeRetourne200() throws Exception {
		long id = creerInscription(109L, 209L);
		mockMvc.perform(patch(BASE_URL + "/" + id + "/confirmer"))
				.andExpect(status().isOk());

		mockMvc.perform(patch(BASE_URL + "/" + id + "/presence"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.presence").value(true));
	}

	@Test
	void enregistrerPresencePourInscriptionNonConfirmeeRetourne409() throws Exception {
		long id = creerInscription(110L, 210L);

		mockMvc.perform(patch(BASE_URL + "/" + id + "/presence"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409));
	}

	@Test
	void requeteAvecIdentifiantBenevoleNullRetourne400AvecDetailDuChamp() throws Exception {
		mockMvc.perform(post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
							{"identifiantBenevole":null,"identifiantEvenement":211}
							"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString(
						"identifiantBenevole")));
	}

	@Test
	void corpsJsonIllisibleRetourne400() throws Exception {
		mockMvc.perform(post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content("{json invalide"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message").value("Le corps JSON est illisible ou invalide."));
	}

	@Test
	void identifiantNonNumeriqueDansUrlRetourne400() throws Exception {
		mockMvc.perform(get(BASE_URL + "/pas-un-nombre"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("id")));
	}

	@Test
	void modifierInscriptionRetourne200() throws Exception {
		long id = creerInscription(111L, 212L);

		mockMvc.perform(put(BASE_URL + "/" + id)
					.contentType(MediaType.APPLICATION_JSON)
					.content(corps(112L, 213L)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.identifiantBenevole").value(112))
				.andExpect(jsonPath("$.identifiantEvenement").value(213));
	}

	@Test
	void supprimerInscriptionExistanteRetourne204() throws Exception {
		long id = creerInscription(113L, 214L);

		mockMvc.perform(delete(BASE_URL + "/" + id))
				.andExpect(status().isNoContent());

		mockMvc.perform(get(BASE_URL + "/" + id))
				.andExpect(status().isNotFound());
	}

	@Test
	void supprimerInscriptionInexistanteRetourne404() throws Exception {
		mockMvc.perform(delete(BASE_URL + "/999999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	private long creerInscription(long identifiantBenevole, long identifiantEvenement) throws Exception {
		MvcResult result = mockMvc.perform(post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(corps(identifiantBenevole, identifiantEvenement)))
				.andExpect(status().isCreated())
				.andReturn();

		JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
		return response.get("id").asLong();
	}

	private String corps(long identifiantBenevole, long identifiantEvenement) throws Exception {
		return objectMapper.writeValueAsString(
				new InscriptionRequest(identifiantBenevole, identifiantEvenement));
	}
}
