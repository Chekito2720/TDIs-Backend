package com.tdis.tramites.service;

import com.tdis.common.dto.ActividadDTO;
import com.tdis.common.dto.CrearSolicitudRequest;
import com.tdis.common.dto.RevisarSolicitudRequest;
import com.tdis.common.dto.SolicitudDTO;
import com.tdis.common.enums.EstadoSolicitud;
import com.tdis.common.exception.BadRequestException;
import com.tdis.common.exception.ResourceNotFoundException;
import com.tdis.tramites.client.CatalogoClient;
import com.tdis.tramites.entity.Solicitud;
import com.tdis.tramites.repository.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TramiteService {

    private final SolicitudRepository solicitudRepository;
    private final CatalogoClient catalogoClient;

    public List<SolicitudDTO> listarPorAlumno(UUID alumnoId) {
        return solicitudRepository.findByAlumnoIdOrderByCreatedAtDesc(alumnoId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<SolicitudDTO> listarPorEstado(EstadoSolicitud estado) {
        return solicitudRepository.findByEstadoOrderByCreatedAtDesc(estado).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<SolicitudDTO> listarTodas() {
        return solicitudRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public SolicitudDTO obtenerPorId(UUID id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));
        return toDTO(solicitud);
    }

    @Transactional
    public SolicitudDTO crear(UUID alumnoId, CrearSolicitudRequest request) {
        ActividadDTO actividad = catalogoClient.obtenerActividad(request.getActividadId());

        if (!actividad.getActiva()) {
            throw new BadRequestException("La actividad no esta disponible");
        }

        boolean yaEnviada = solicitudRepository
                .existsByAlumnoIdAndActividadIdAndEstado(alumnoId, request.getActividadId(), EstadoSolicitud.EN_REVISION);
        if (yaEnviada) {
            throw new BadRequestException("Ya tienes una solicitud en revision para esta actividad");
        }

        Solicitud solicitud = new Solicitud();
        solicitud.setAlumnoId(alumnoId);
        solicitud.setActividadId(request.getActividadId());
        solicitud.setTipoSolicitud(request.getTipoSolicitud());
        solicitud.setDescripcion(request.getDescripcion());
        solicitud.setReflexion(request.getReflexion());
        solicitud.setLugar(request.getLugar());
        solicitud.setHoras(request.getHoras());
        solicitud.setTipoActividad(request.getTipoActividad());
        solicitud.setMateriaRelacionada(request.getMateriaRelacionada());
        solicitud.setDivision(request.getDivision());
        solicitud.setPrograma(request.getPrograma());
        solicitud.setGrupo(request.getGrupo());
        solicitud.setCuatrimestre(request.getCuatrimestre());
        solicitud.setTurno(request.getTurno());
        solicitud.setTutor(request.getTutor());
        solicitud.setNombreResponsable(request.getNombreResponsable());
        solicitud.setCargoResponsable(request.getCargoResponsable());
        solicitud.setTelefonoResponsable(request.getTelefonoResponsable());
        solicitud.setCorreoResponsable(request.getCorreoResponsable());
        solicitud.setEstado(EstadoSolicitud.EN_REVISION);

        solicitud = solicitudRepository.save(solicitud);
        return toDTO(solicitud);
    }

    @Transactional
    public SolicitudDTO revisar(UUID id, RevisarSolicitudRequest request) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        if (solicitud.getEstado() != EstadoSolicitud.EN_REVISION) {
            throw new BadRequestException("La solicitud ya fue revisada");
        }

        if (request.getEstado() == EstadoSolicitud.RECHAZADA) {
            if (request.getComentario() == null || request.getComentario().isBlank()) {
                throw new BadRequestException("Debe proporcionar un comentario al rechazar");
            }
            solicitud.setComentarioRechazo(request.getComentario());
        }

        solicitud.setEstado(request.getEstado());
        solicitud = solicitudRepository.save(solicitud);
        return toDTO(solicitud);
    }

    public List<SolicitudDTO> listarPorAlumnoYEstado(UUID alumnoId, EstadoSolicitud estado) {
        return solicitudRepository.findByAlumnoIdAndEstado(alumnoId, estado).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private SolicitudDTO toDTO(Solicitud solicitud) {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setId(solicitud.getId());
        dto.setAlumnoId(solicitud.getAlumnoId());
        dto.setTipoSolicitud(solicitud.getTipoSolicitud());
        dto.setActividadId(solicitud.getActividadId());
        dto.setDescripcion(solicitud.getDescripcion());
        dto.setReflexion(solicitud.getReflexion());
        dto.setLugar(solicitud.getLugar());
        dto.setHoras(solicitud.getHoras());
        dto.setTipoActividad(solicitud.getTipoActividad());
        dto.setMateriaRelacionada(solicitud.getMateriaRelacionada());
        dto.setDivision(solicitud.getDivision());
        dto.setPrograma(solicitud.getPrograma());
        dto.setGrupo(solicitud.getGrupo());
        dto.setCuatrimestre(solicitud.getCuatrimestre());
        dto.setTurno(solicitud.getTurno());
        dto.setTutor(solicitud.getTutor());
        dto.setNombreResponsable(solicitud.getNombreResponsable());
        dto.setCargoResponsable(solicitud.getCargoResponsable());
        dto.setTelefonoResponsable(solicitud.getTelefonoResponsable());
        dto.setCorreoResponsable(solicitud.getCorreoResponsable());
        dto.setEstado(solicitud.getEstado());
        dto.setComentarioRechazo(solicitud.getComentarioRechazo());
        dto.setArchivoPath(solicitud.getArchivoPath());
        dto.setNombreArchivo(solicitud.getNombreArchivo());
        dto.setCreatedAt(solicitud.getCreatedAt());
        dto.setUpdatedAt(solicitud.getUpdatedAt());

        try {
            ActividadDTO actividad = catalogoClient.obtenerActividad(solicitud.getActividadId());
            dto.setActividadTitulo(actividad.getTitulo());
            dto.setActividadEje(actividad.getEje().name());
            dto.setActividadPuntos(actividad.getPuntosTdi());
        } catch (Exception e) {
            dto.setActividadTitulo("Actividad no disponible");
        }

        return dto;
    }
}
