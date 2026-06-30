package com.tdis.progreso.service;

import com.tdis.common.dto.ActividadDTO;
import com.tdis.common.dto.ProgresoDTO;
import com.tdis.common.dto.SolicitudDTO;
import com.tdis.common.enums.EstadoSolicitud;
import com.tdis.common.enums.NivelProgreso;
import com.tdis.common.exception.ResourceNotFoundException;
import com.tdis.progreso.client.CatalogoClient;
import com.tdis.progreso.client.TramitesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgresoService {

    private static final int PUNTOS_SENSIBILIZADOR = 0;
    private static final int PUNTOS_FORMATIVO = 100;
    private static final int PUNTOS_APLICATIVO = 250;
    private static final int PUNTOS_IMPLEMENTADOR = 500;

    private final TramitesClient tramitesClient;
    private final CatalogoClient catalogoClient;

    public ProgresoDTO calcularProgreso(UUID alumnoId) {
        List<SolicitudDTO> solicitudes;
        try {
            solicitudes = tramitesClient.listarPorAlumno(alumnoId);
        } catch (Exception e) {
            log.error("Error al obtener solicitudes del alumno {}: {}", alumnoId, e.getMessage());
            throw new ResourceNotFoundException("No se pudo obtener el progreso del alumno");
        }

        List<SolicitudDTO> aprobadas = solicitudes.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.APROBADA)
                .toList();

        long enRevision = solicitudes.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.EN_REVISION)
                .count();

        int puntosTotales = 0;
        Map<String, Integer> puntosPorEje = new HashMap<>();
        puntosPorEje.put("ENTORNO_SOCIAL", 0);
        puntosPorEje.put("CULTURAL", 0);
        puntosPorEje.put("DEPORTIVO", 0);
        puntosPorEje.put("TRASCENDENCIA", 0);

        for (SolicitudDTO solicitud : aprobadas) {
            try {
                ActividadDTO actividad = catalogoClient.obtenerActividad(solicitud.getActividadId());
                puntosTotales += actividad.getPuntosTdi();

                String ejeKey = actividad.getEje().name();
                puntosPorEje.merge(ejeKey, actividad.getPuntosTdi(), Integer::sum);
            } catch (Exception e) {
                log.warn("No se pudo obtener actividad {}: {}", solicitud.getActividadId(), e.getMessage());
            }
        }

        NivelProgreso nivel = calcularNivel(puntosTotales);

        ProgresoDTO progreso = new ProgresoDTO();
        progreso.setAlumnoId(alumnoId);
        progreso.setPuntosTotales(puntosTotales);
        progreso.setNivelActual(nivel);
        progreso.setPuntosPorEje(puntosPorEje);
        progreso.setActividadesCompletadas((long) aprobadas.size());
        progreso.setActividadesEnRevision(enRevision);

        return progreso;
    }

    private NivelProgreso calcularNivel(int puntos) {
        if (puntos >= PUNTOS_IMPLEMENTADOR) return NivelProgreso.IMPLEMENTADOR;
        if (puntos >= PUNTOS_APLICATIVO) return NivelProgreso.APLICATIVO;
        if (puntos >= PUNTOS_FORMATIVO) return NivelProgreso.FORMATIVO;
        return NivelProgreso.SENSIBILIZADOR;
    }
}
