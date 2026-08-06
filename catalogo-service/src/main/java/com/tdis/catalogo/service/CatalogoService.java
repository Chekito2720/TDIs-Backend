package com.tdis.catalogo.service;

import com.tdis.catalogo.entity.Actividad;
import com.tdis.catalogo.repository.ActividadRepository;
import com.tdis.common.dto.ActividadDTO;
import com.tdis.common.enums.EstadoRevision;
import com.tdis.common.enums.EjeFormativo;
import com.tdis.common.exception.BadRequestException;
import com.tdis.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final ActividadRepository actividadRepository;

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
        actividad.setTitulo(dto.getTitulo());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setEje(dto.getEje());
        actividad.setPuntosTdi(dto.getPuntosTdi());
        actividad.setPeriodicidad(dto.getPeriodicidad());
        actividad.setFechaInicio(dto.getFechaInicio());
        actividad.setFechaFin(dto.getFechaFin());
        actividad.setCreadorId(dto.getCreadorId());
        actividad.setCreadorTipo(dto.getCreadorTipo());
        actividad.setActiva(false);
        actividad.setEstadoRevision(EstadoRevision.PENDIENTE);
        actividad = actividadRepository.save(actividad);
        return toDTO(actividad);
    }

    public ActividadDTO actualizar(UUID id, ActividadDTO dto) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        actividad.setTitulo(dto.getTitulo());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setEje(dto.getEje());
        actividad.setPuntosTdi(dto.getPuntosTdi());
        actividad.setPeriodicidad(dto.getPeriodicidad());
        actividad.setFechaInicio(dto.getFechaInicio());
        actividad.setFechaFin(dto.getFechaFin());
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
        dto.setActiva(actividad.getActiva());
        dto.setEstadoRevision(actividad.getEstadoRevision());
        dto.setCreadorId(actividad.getCreadorId());
        dto.setCreadorTipo(actividad.getCreadorTipo());
        dto.setComentarioRevision(actividad.getComentarioRevision());
        dto.setCreatedAt(actividad.getCreatedAt());
        dto.setUpdatedAt(actividad.getUpdatedAt());
        return dto;
    }
}
