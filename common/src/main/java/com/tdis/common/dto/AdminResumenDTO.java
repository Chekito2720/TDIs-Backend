package com.tdis.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResumenDTO {
    private long totalAlumnos;
    private long actividadesAprobadas;
    private long actividadesRechazadas;
    private int puntosDistribuidos;
    private Map<String, Long> distribucionNiveles;
    private Map<String, Integer> puntosPorEje;
    private List<AlumnoResumenDTO> topAlumnos;
}
