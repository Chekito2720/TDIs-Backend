package com.tdis.catalogo.service;

import com.tdis.catalogo.client.TramitesClient;
import com.tdis.catalogo.entity.Actividad;
import com.tdis.catalogo.repository.ActividadRepository;
import com.tdis.common.dto.ActividadDTO;
import com.tdis.common.dto.SolicitudDTO;
import com.tdis.common.enums.AsignaturaFormacion;
import com.tdis.common.enums.CompetenciaReforzada;
import com.tdis.common.enums.DimensionFormacion;
import com.tdis.common.enums.EstadoRevision;
import com.tdis.common.enums.EstadoSolicitud;
import com.tdis.common.enums.EjeFormativo;
import com.tdis.common.enums.NivelImpacto;
import com.tdis.common.enums.Periodicidad;
import com.tdis.common.enums.PublicoObjetivo;
import com.tdis.common.enums.TipoEvidenciaRequerida;
import com.tdis.common.enums.TipoLugar;
import com.tdis.common.exception.BadRequestException;
import com.tdis.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final ActividadRepository actividadRepository;
    private final TramitesClient tramitesClient;

    public List<ActividadDTO> listarActivas() {
        return actividadRepository.findByActivaTrueAndEstadoRevision(EstadoRevision.APROBADA).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ActividadDTO> listarTodas() {
        return actividadRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ActividadDTO> listarPorEstadoRevision(EstadoRevision estado) {
        return actividadRepository.findByEstadoRevision(estado).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ActividadDTO> listarPorEje(EjeFormativo eje) {
        return actividadRepository.findByEjeAndActivaTrueAndEstadoRevision(eje, EstadoRevision.APROBADA).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ActividadDTO> obtenerPorCreador(UUID creadorId) {
        return actividadRepository.findByCreadorId(creadorId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ActividadDTO obtenerPorId(UUID id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        return toDTO(actividad);
    }

    public ActividadDTO crear(ActividadDTO dto) {
        Actividad actividad = new Actividad();
        mapDTOToEntity(dto, actividad);
        actividad.setActiva(false);
        actividad.setEstadoRevision(EstadoRevision.PENDIENTE);
        actividad = actividadRepository.save(actividad);
        return toDTO(actividad);
    }

    public ActividadDTO actualizar(UUID id, ActividadDTO dto) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        mapDTOToEntity(dto, actividad);
        actividad = actividadRepository.save(actividad);
        return toDTO(actividad);
    }

    public void desactivar(UUID id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        actividad.setActiva(false);
        actividadRepository.save(actividad);
    }

    public void activar(UUID id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        if (actividad.getEstadoRevision() != EstadoRevision.APROBADA) {
            throw new BadRequestException("Solo se pueden activar actividades aprobadas");
        }
        actividad.setActiva(true);
        actividadRepository.save(actividad);
    }

    public ActividadDTO revisar(UUID id, EstadoRevision estado, String comentario) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        if (estado == EstadoRevision.RECHAZADA && (comentario == null || comentario.isBlank())) {
            throw new BadRequestException("Debe proporcionar un comentario al rechazar");
        }
        actividad.setEstadoRevision(estado);
        actividad.setComentarioRevision(comentario);
        if (estado == EstadoRevision.RECHAZADA) {
            actividad.setActiva(false);
        }
        actividad = actividadRepository.save(actividad);
        return toDTO(actividad);
    }

    private void mapDTOToEntity(ActividadDTO dto, Actividad actividad) {
        actividad.setTitulo(dto.getTitulo());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setEje(dto.getEje());
        actividad.setPuntosTdi(dto.getPuntosTdi());
        actividad.setPeriodicidad(dto.getPeriodicidad());
        actividad.setFechaInicio(dto.getFechaInicio());
        actividad.setFechaFin(dto.getFechaFin());
        actividad.setHorasEfectivas(dto.getHorasEfectivas());
        actividad.setLugar(dto.getLugar());
        actividad.setDimensionesFormacion(enumListToString(dto.getDimensionesFormacion()));
        actividad.setNivelImpacto(dto.getNivelImpacto());
        actividad.setPublicoObjetivo(enumListToString(dto.getPublicoObjetivo()));
        actividad.setAsignaturasRelacionadas(enumListToString(dto.getAsignaturasRelacionadas()));
        actividad.setCompetenciasReforzar(enumListToString(dto.getCompetenciasReforzar()));
        actividad.setTiposEvidenciaRequerida(enumListToString(dto.getTiposEvidenciaRequerida()));
        actividad.setCreadorId(dto.getCreadorId());
        actividad.setCreadorTipo(dto.getCreadorTipo());
    }

    private String enumListToString(List<? extends Enum<?>> list) {
        if (list == null || list.isEmpty()) return null;
        return list.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    private <E extends Enum<E>> List<E> stringToEnumList(String value, Class<E> enumClass) {
        if (value == null || value.isBlank()) return List.of();
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> Enum.valueOf(enumClass, s))
                .collect(Collectors.toList());
    }

    private ActividadDTO toDTO(Actividad actividad) {
        ActividadDTO dto = new ActividadDTO();
        dto.setId(actividad.getId());
        dto.setTitulo(actividad.getTitulo());
        dto.setDescripcion(actividad.getDescripcion());
        dto.setEje(actividad.getEje());
        dto.setPuntosTdi(actividad.getPuntosTdi());
        dto.setPeriodicidad(actividad.getPeriodicidad());
        dto.setFechaInicio(actividad.getFechaInicio());
        dto.setFechaFin(actividad.getFechaFin());
        dto.setHorasEfectivas(actividad.getHorasEfectivas());
        dto.setLugar(actividad.getLugar());
        dto.setDimensionesFormacion(stringToEnumList(actividad.getDimensionesFormacion(), DimensionFormacion.class));
        dto.setNivelImpacto(actividad.getNivelImpacto());
        dto.setPublicoObjetivo(stringToEnumList(actividad.getPublicoObjetivo(), PublicoObjetivo.class));
        dto.setAsignaturasRelacionadas(stringToEnumList(actividad.getAsignaturasRelacionadas(), AsignaturaFormacion.class));
        dto.setCompetenciasReforzar(stringToEnumList(actividad.getCompetenciasReforzar(), CompetenciaReforzada.class));
        dto.setTiposEvidenciaRequerida(stringToEnumList(actividad.getTiposEvidenciaRequerida(), TipoEvidenciaRequerida.class));
        dto.setActiva(actividad.getActiva());
        dto.setEstadoRevision(actividad.getEstadoRevision());
        dto.setCreadorId(actividad.getCreadorId());
        dto.setCreadorTipo(actividad.getCreadorTipo());
        dto.setComentarioRevision(actividad.getComentarioRevision());
        dto.setCreatedAt(actividad.getCreatedAt());
        dto.setUpdatedAt(actividad.getUpdatedAt());
        return dto;
    }

public ActividadDTO crearDesdePrevia(UUID solicitudId, UUID creadorId, String creadorTipo, Integer puntosTdi) {
        SolicitudDTO solicitud = tramitesClient.obtenerSolicitud(solicitudId);

        if (solicitud == null) {
            throw new ResourceNotFoundException("Solicitud no encontrada");
        }
        if (!EstadoSolicitud.APROBADA.name().equals(solicitud.getEstado())) {
            throw new BadRequestException("Solo se pueden convertir solicitudes aprobadas");
        }
        if (solicitud.getEje() == null || puntosTdi == null) {
            throw new BadRequestException("La solicitud no tiene los campos necesarios para crear una actividad");
        }

        Actividad actividad = new Actividad();
        actividad.setTitulo(solicitud.getNombreActividad());
        actividad.setDescripcion(solicitud.getDescripcion());
        actividad.setEje(solicitud.getEje());
        actividad.setPuntosTdi(puntosTdi);
        actividad.setPeriodicidad(solicitud.getPeriodicidad() != null ? Periodicidad.valueOf(solicitud.getPeriodicidad()) : Periodicidad.UNICA);
        actividad.setFechaInicio(solicitud.getFechaInicio() != null ? java.time.LocalDate.parse(solicitud.getFechaInicio()) : null);
        actividad.setFechaFin(solicitud.getFechaFin() != null ? java.time.LocalDate.parse(solicitud.getFechaFin()) : null);
        actividad.setHorasEfectivas(solicitud.getHorasEfectivas());
        actividad.setLugar(solicitud.getTipoLugar() != null ? solicitud.getTipoLugar() : TipoLugar.INTERNO);
        actividad.setDimensionesFormacion(solicitud.getDimensionesFormacion());
        actividad.setNivelImpacto(solicitud.getNivelImpacto() != null ? NivelImpacto.valueOf(solicitud.getNivelImpacto()) : null);
        actividad.setPublicoObjetivo(solicitud.getPublicoObjetivo());
        actividad.setAsignaturasRelacionadas(solicitud.getAsignaturasRelacionadas());
        actividad.setCompetenciasReforzar(solicitud.getCompetenciasReforzar());
        actividad.setTiposEvidenciaRequerida(solicitud.getEvidenciasRequeridas());
        actividad.setCreadorId(creadorId);
        actividad.setCreadorTipo(creadorTipo);
        actividad.setActiva(false);
        actividad.setEstadoRevision(EstadoRevision.PENDIENTE);

        actividad = actividadRepository.save(actividad);
        return toDTO(actividad);
    }
}