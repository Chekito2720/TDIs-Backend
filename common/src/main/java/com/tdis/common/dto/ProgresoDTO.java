package com.tdis.common.dto;

import com.tdis.common.enums.NivelProgreso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgresoDTO {
    private UUID alumnoId;
    private String alumnoNombre;
    private Integer puntosTotales;
    private NivelProgreso nivelActual;
    private Map<String, Integer> puntosPorEje;
    private Long actividadesCompletadas;
    private Long actividadesEnRevision;
}
