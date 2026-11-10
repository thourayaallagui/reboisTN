package tn.esprit.reboistn.notifications;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test MockMvc pour le endpoint GET /api/notifications.
 */
@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnNotificationsForUser() throws Exception {
        mockMvc.perform(get("/api/notifications")
                        .header("X-User-Id", "user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].destinataireId").value("user-1"));
    }

    @Test
    void shouldReturnUnreadCount() throws Exception {
        mockMvc.perform(get("/api/notifications/non-lues")
                        .header("X-User-Id", "user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destinataireId").value("user-1"))
                .andExpect(jsonPath("$.count").value(2));
    }

    @Test
    void shouldReturnEmptyListForUnknownUser() throws Exception {
        mockMvc.perform(get("/api/notifications")
                        .header("X-User-Id", "user-unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

