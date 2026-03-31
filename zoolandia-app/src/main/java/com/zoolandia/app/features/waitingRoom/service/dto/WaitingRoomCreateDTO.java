package com.zoolandia.app.features.waitingRoom.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.jspecify.annotations.Nullable;

public record WaitingRoomCreateDTO(
    @NotNull(message = "El cliente es requerido")
    Long clientId,
    
    @NotNull(message = "La mascota es requerida") 
    Long petId,
    
    @NotNull(message = "La razón de la visita es requerida")
    String reasonForVisit,
    
    @Min(value = 1, message = "La prioridad mínima es 1")
    @Max(value = 3, message = "La prioridad máxima es 3")
    Integer priority,
    
    @Nullable
    String notes
) {}