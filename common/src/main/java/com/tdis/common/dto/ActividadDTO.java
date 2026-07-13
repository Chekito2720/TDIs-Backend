package com.tdis.common.dto;

import com.tdis.common.enums.EjeFormativo;
import com.tdis.common.enums.Periodicidad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActividadDTO {
    private UUID id;
    private String titulo;
    private String descripcion;
    private EjeFormativo eje;
    private Integer puntosTdi;
    private Periodicidad periodicidad;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activa;
    private LocalDateTime createdAt;
}
