package com.reboistn.event.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Statuts d'un événement dans son cycle de vie")
public enum StatutEvenement {
    BROUILLON,
    PUBLIE,
    ANNULE,
    TERMINE
}
