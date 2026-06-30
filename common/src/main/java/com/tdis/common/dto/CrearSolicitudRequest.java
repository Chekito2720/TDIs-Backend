package com.tdis.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearSolicitudRequest {
    @NotNull(message = "El ID de la actividad es requerido")
    private UUID actividadId;
}
