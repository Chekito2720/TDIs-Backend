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

        if (request.getEstado() == EstadoSolicitud.RECHAZADA && solicitud.getArchivoPath() != null) {
            // El archivo se elimina via el client si es necesario
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

    public long contarAprobadas(UUID alumnoId) {
        return solicitudRepository.countByAlumnoIdAndEstado(alumnoId, EstadoSolicitud.APROBADA);
    }

    private SolicitudDTO toDTO(Solicitud solicitud) {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setId(solicitud.getId());
        dto.setAlumnoId(solicitud.getAlumnoId());
        dto.setActividadId(solicitud.getActividadId());
        dto.setEstado(solicitud.getEstado());
        dto.setComentarioRechazo(solicitud.getComentarioRechazo());
        dto.setArchivoPath(solicitud.getArchivoPath());
        dto.setCreatedAt(solicitud.getCreatedAt());

        try {
            ActividadDTO actividad = catalogoClient.obtenerActividad(solicitud.getActividadId());
            dto.setActividadTitulo(actividad.getTitulo());
        } catch (Exception e) {
            dto.setActividadTitulo("Actividad no disponible");
        }

        return dto;
    }
}
