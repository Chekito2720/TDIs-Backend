package com.tdis.common.dto;

import com.tdis.common.enums.EjeFormativo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String temporalidad;
    private Boolean activa;
}
