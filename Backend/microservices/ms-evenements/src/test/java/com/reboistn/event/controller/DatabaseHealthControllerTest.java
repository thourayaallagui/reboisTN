package com.reboistn.event.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "eureka.client.enabled=false")
@AutoConfigureMockMvc
class DatabaseHealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDatabaseCheckEndpoint() throws Exception {
        mockMvc.perform(get("/api/events/db-check"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.database").value("H2"))
                .andExpect(jsonPath("$.status").value("CONNECTED"))
                .andExpect(jsonPath("$.result").value(1));
    }
}
