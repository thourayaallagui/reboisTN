package com.reboistn.event.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Types d'événements écologiques supportés par ReboisTN")
public enum TypeEvenement {
    PLANTATION,
    NETTOYAGE,
    SENSIBILISATION,
    RECYCLAGE,
    AUTRE
}
