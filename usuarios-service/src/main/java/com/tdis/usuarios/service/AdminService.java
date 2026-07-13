package com.tdis.usuarios.service;

import com.tdis.common.dto.*;
import com.tdis.common.enums.EstadoSolicitud;
import com.tdis.common.enums.NivelProgreso;
import com.tdis.common.enums.TipoUsuario;
import com.tdis.usuarios.client.CatalogoClient;
import com.tdis.usuarios.client.TramitesClient;
import com.tdis.usuarios.entity.Usuario;
import com.tdis.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final TramitesClient tramitesClient;
    private final CatalogoClient catalogoClient;

    public AdminResumenDTO obtenerResumen() {
        List<Usuario> alumnos = usuarioRepository.findByTipoUsuario(TipoUsuario.ALUMNO);
        List<SolicitudDTO> todas = tramitesClient.listarTodas();

        long aprobadas = todas.stream().filter(s -> s.getEstado() == EstadoSolicitud.APROBADA).count();
        long rechazadas = todas.stream().filter(s -> s.getEstado() == EstadoSolicitud.RECHAZADA).count();

        int puntosDistribuidos = 0;
        Map<String, Integer> puntosPorEje = new HashMap<>();
        Map<String, Integer> distribucionNiveles = new HashMap<>();
        List<AlumnoResumenDTO> topAlumnos = new ArrayList<>();

        // Compute levels for each student based on approved solicitudes
        Map<UUID, Integer> puntosPorAlumno = new HashMap<>();
        Map<UUID, Map<String, Integer>> puntosEjePorAlumno = new HashMap<>();

        List<SolicitudDTO> aprobadasList = todas.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.APROBADA)
                .toList();

        for (SolicitudDTO sol : aprobadasList) {
            try {
                ActividadDTO act = catalogoClient.obtenerActividad(sol.getActividadId());
                puntosDistribuidos += act.getPuntosTdi();
                puntosPorEje.merge(act.getEje().name(), act.getPuntosTdi(), Integer::sum);

                puntosPorAlumno.merge(sol.getAlumnoId(), act.getPuntosTdi(), Integer::sum);
                puntosEjePorAlumno.computeIfAbsent(sol.getAlumnoId(), k -> new HashMap<>());
                puntosEjePorAlumno.get(sol.getAlumnoId()).merge(act.getEje().name(), act.getPuntosTdi(), Integer::sum);
            } catch (Exception ignored) {}
        }

        for (Usuario alumno : alumnos) {
            int pts = puntosPorAlumno.getOrDefault(alumno.getId(), 0);
            NivelProgreso nivel = calcularNivel(pts);
            distribucionNiveles.merge(nivel.name(), 1, Integer::sum);

            Map<String, Integer> ejes = puntosEjePorAlumno.getOrDefault(alumno.getId(), new HashMap<>());
            topAlumnos.add(new AlumnoResumenDTO(
                    alumno.getId(),
                    alumno.getMatricula(),
                    alumno.getNombre(),
                    alumno.getApellidos(),
                    nivel.name(),
                    ejes.getOrDefault("PERSONAL", 0),
                    ejes.getOrDefault("ENTORNO_SOCIAL", 0),
                    ejes.getOrDefault("DEPORTIVO", 0),
                    ejes.getOrDefault("TRASCENDENCIA", 0),
                    pts,
                    alumno.getCreatedAt()
            ));
        }

        topAlumnos.sort((a, b) -> b.getTotal() - a.getTotal());
        if (topAlumnos.size() > 10) topAlumnos = topAlumnos.subList(0, 10);

        AdminResumenDTO resumen = new AdminResumenDTO();
        resumen.setTotalAlumnos(alumnos.size());
        resumen.setActividadesAprobadas(aprobadas);
        resumen.setActividadesRechazadas(rechazadas);
        resumen.setPuntosDistribuidos(puntosDistribuidos);
        resumen.setDistribucionNiveles(distribucionNiveles);
        resumen.setPuntosPorEje(puntosPorEje);
        resumen.setTopAlumnos(topAlumnos);

        return resumen;
    }

    public List<AlumnoResumenDTO> listarAlumnos() {
        List<Usuario> alumnos = usuarioRepository.findByTipoUsuario(TipoUsuario.ALUMNO);
        List<SolicitudDTO> todas = tramitesClient.listarTodas();

        Map<UUID, Integer> puntosPorAlumno = new HashMap<>();
        Map<UUID, Map<String, Integer>> puntosEjePorAlumno = new HashMap<>();

        List<SolicitudDTO> aprobadas = todas.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.APROBADA)
                .toList();

        for (SolicitudDTO sol : aprobadas) {
            try {
                ActividadDTO act = catalogoClient.obtenerActividad(sol.getActividadId());
                puntosPorAlumno.merge(sol.getAlumnoId(), act.getPuntosTdi(), Integer::sum);
                puntosEjePorAlumno.computeIfAbsent(sol.getAlumnoId(), k -> new HashMap<>());
                puntosEjePorAlumno.get(sol.getAlumnoId()).merge(act.getEje().name(), act.getPuntosTdi(), Integer::sum);
            } catch (Exception ignored) {}
        }

        List<AlumnoResumenDTO> result = new ArrayList<>();
        for (Usuario alumno : alumnos) {
            int pts = puntosPorAlumno.getOrDefault(alumno.getId(), 0);
            Map<String, Integer> ejes = puntosEjePorAlumno.getOrDefault(alumno.getId(), new HashMap<>());
            result.add(new AlumnoResumenDTO(
                    alumno.getId(),
                    alumno.getMatricula(),
                    alumno.getNombre(),
                    alumno.getApellidos(),
                    calcularNivel(pts).name(),
                    ejes.getOrDefault("PERSONAL", 0),
                    ejes.getOrDefault("ENTORNO_SOCIAL", 0),
                    ejes.getOrDefault("DEPORTIVO", 0),
                    ejes.getOrDefault("TRASCENDENCIA", 0),
                    pts,
                    alumno.getCreatedAt()
            ));
        }

        return result;
    }

    private NivelProgreso calcularNivel(int puntos) {
        if (puntos >= 1000) return NivelProgreso.EMBAJADOR;
        if (puntos >= 601) return NivelProgreso.LIDER;
        if (puntos >= 301) return NivelProgreso.PROMOTOR;
        return NivelProgreso.EXPLORADOR;
    }
}
