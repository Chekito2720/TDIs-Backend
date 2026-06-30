package com.tdis.common.dto;

import com.tdis.common.enums.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevisarSolicitudRequest {
    @NotNull(message = "El estado es requerido")
    private EstadoSolicitud estado;

    private String comentario;
}
