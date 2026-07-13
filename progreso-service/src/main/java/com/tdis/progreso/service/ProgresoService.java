package com.tdis.progreso.service;

import com.tdis.common.dto.ActividadDTO;
import com.tdis.common.dto.ProgresoDTO;
import com.tdis.common.dto.SolicitudDTO;
import com.tdis.common.dto.UsuarioDTO;
import com.tdis.common.enums.EstadoSolicitud;
import com.tdis.common.enums.NivelProgreso;
import com.tdis.common.exception.ResourceNotFoundException;
import com.tdis.progreso.client.CatalogoClient;
import com.tdis.progreso.client.TramitesClient;
import com.tdis.progreso.client.UsuariosClient;
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

    private static final int PUNTOS_EXPLORADOR = 0;
    private static final int PUNTOS_PROMOTOR = 301;
    private static final int PUNTOS_LIDER = 601;
    private static final int PUNTOS_EMBAJADOR = 1000;

    private final TramitesClient tramitesClient;
    private final CatalogoClient catalogoClient;
    private final UsuariosClient usuariosClient;

    public ProgresoDTO calcularProgreso(UUID alumnoId) {
        List<SolicitudDTO> solicitudes;
        try {
            solicitudes = tramitesClient.listarPorAlumno(alumnoId);
        } catch (Exception e) {
            log.error("Error al obtener solicitudes del alumno {}: {}", alumnoId, e.getMessage());
            throw new ResourceNotFoundException("No se pudo obtener el progreso del alumno");
        }

        UsuarioDTO usuario = null;
        try {
            usuario = usuariosClient.obtenerPorId(alumnoId);
        } catch (Exception e) {
            log.warn("No se pudo obtener datos del usuario {}: {}", alumnoId, e.getMessage());
        }

        return buildProgreso(alumnoId, usuario, solicitudes);
    }

    public ProgresoDTO calcularProgresoPorMatricula(String matricula) {
        UsuarioDTO usuario;
        try {
            usuario = usuariosClient.obtenerPorMatricula(matricula);
        } catch (Exception e) {
            log.error("Error al obtener usuario por matricula {}: {}", matricula, e.getMessage());
            throw new ResourceNotFoundException("Alumno no encontrado con matricula: " + matricula);
        }

        List<SolicitudDTO> solicitudes;
        try {
            solicitudes = tramitesClient.listarPorAlumno(usuario.getId());
        } catch (Exception e) {
            log.error("Error al obtener solicitudes del alumno {}: {}", usuario.getId(), e.getMessage());
            throw new ResourceNotFoundException("No se pudo obtener el progreso del alumno");
        }

        return buildProgreso(usuario.getId(), usuario, solicitudes);
    }

    private ProgresoDTO buildProgreso(UUID alumnoId, UsuarioDTO usuario, List<SolicitudDTO> solicitudes) {
        List<SolicitudDTO> aprobadas = solicitudes.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.APROBADA)
                .toList();

        long enRevision = solicitudes.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.EN_REVISION)
                .count();

        int puntosTotales = 0;
        Map<String, Integer> puntosPorEje = new HashMap<>();
        puntosPorEje.put("ENTORNO_SOCIAL", 0);
        puntosPorEje.put("PERSONAL", 0);
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
        int puntosSiguiente = puntosSiguienteNivel(puntosTotales);
        int porcentaje = calcularPorcentaje(puntosTotales);

        ProgresoDTO progreso = new ProgresoDTO();
        progreso.setAlumnoId(alumnoId);
        progreso.setAlumnoMatricula(usuario != null ? usuario.getMatricula() : null);
        progreso.setAlumnoNombre(usuario != null ? usuario.getNombre() + " " + usuario.getApellidos() : null);
        progreso.setPuntosTotales(puntosTotales);
        progreso.setNivelActual(nivel);
        progreso.setPuntosPorEje(puntosPorEje);
        progreso.setActividadesCompletadas((long) aprobadas.size());
        progreso.setActividadesEnRevision(enRevision);
        progreso.setPuntosSiguienteNivel(puntosSiguiente);
        progreso.setPorcentajeProgreso(porcentaje);

        return progreso;
    }

    private int puntosSiguienteNivel(int puntosActuales) {
        if (puntosActuales < PUNTOS_PROMOTOR) return PUNTOS_PROMOTOR - puntosActuales;
        if (puntosActuales < PUNTOS_LIDER) return PUNTOS_LIDER - puntosActuales;
        if (puntosActuales < PUNTOS_EMBAJADOR) return PUNTOS_EMBAJADOR - puntosActuales;
        return 0;
    }

    private int calcularPorcentaje(int puntosTotales) {
        if (puntosTotales >= PUNTOS_EMBAJADOR) return 100;
        if (puntosTotales >= PUNTOS_LIDER)
            return 75 + ((puntosTotales - PUNTOS_LIDER) * 25 / (PUNTOS_EMBAJADOR - PUNTOS_LIDER));
        if (puntosTotales >= PUNTOS_PROMOTOR)
            return 25 + ((puntosTotales - PUNTOS_PROMOTOR) * 50 / (PUNTOS_LIDER - PUNTOS_PROMOTOR));
        return (puntosTotales * 25) / (PUNTOS_PROMOTOR - PUNTOS_EXPLORADOR);
    }

    private NivelProgreso calcularNivel(int puntos) {
        if (puntos >= PUNTOS_EMBAJADOR) return NivelProgreso.EMBAJADOR;
        if (puntos >= PUNTOS_LIDER) return NivelProgreso.LIDER;
        if (puntos >= PUNTOS_PROMOTOR) return NivelProgreso.PROMOTOR;
        return NivelProgreso.EXPLORADOR;
    }
}
