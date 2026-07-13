package com.tdis.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalisisIAResponse {
    private String nombreActividad;
    private String descripcion;
    private String estado;
    private String motivo;
    private String descripcion_analisis;
    private String veredicto_modelo;
    private Boolean fue_rechazado_por_modelo;
}
