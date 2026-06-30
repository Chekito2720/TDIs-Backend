package com.tdis.common.dto;

import com.tdis.common.enums.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {
    private UUID id;
    private UUID alumnoId;
    private String alumnoNombre;
    private UUID actividadId;
    private String actividadTitulo;
    private EstadoSolicitud estado;
    private String comentarioRechazo;
    private String archivoPath;
    private LocalDateTime createdAt;
}
