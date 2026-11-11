package com.reboistn.event.service;

import com.reboistn.event.dto.EvenementRequest;
import com.reboistn.event.dto.EvenementResponse;
import com.reboistn.event.enums.StatutEvenement;
import com.reboistn.event.enums.TypeEvenement;
import com.reboistn.event.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "eureka.client.enabled=false")
class EvenementServiceTest {

    @Autowired
    private EvenementService service;

    @Test
    void verifyEventValidationRules() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);

        EvenementRequest badDates = new EvenementRequest(
                "Titre", "Desc", TypeEvenement.PLANTATION,
                end, start, "Lieu", "Tunis", 36.0, 10.0, 10, null, 1L);
        assertThrows(BusinessException.class, () -> service.createEvenement(badDates));

        EvenementRequest badCapacity = new EvenementRequest(
                "Titre", "Desc", TypeEvenement.PLANTATION,
                start, end, "Lieu", "Tunis", 36.0, 10.0, 0, null, 1L);
        assertThrows(BusinessException.class, () -> service.createEvenement(badCapacity));

        EvenementRequest valid = new EvenementRequest(
                "Campagne", "Description", TypeEvenement.PLANTATION,
                start, end, "Lieu", "Tunis", 36.0, 10.0, 20, null, 1L);
        EvenementResponse created = service.createEvenement(valid);
        assertEquals(StatutEvenement.BROUILLON, created.getStatut());
        assertEquals(0, created.getNombreInscrits());
    }
}
