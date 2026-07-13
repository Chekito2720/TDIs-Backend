package com.tdis.catalogo.service;

import com.tdis.catalogo.entity.Actividad;
import com.tdis.catalogo.repository.ActividadRepository;
import com.tdis.common.dto.ActividadDTO;
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
        return actividadRepository.findByActivaTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ActividadDTO> listarTodas() {
        return actividadRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ActividadDTO> listarPorEje(EjeFormativo eje) {
        return actividadRepository.findByEjeAndActivaTrue(eje).stream()
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
        actividad.setActiva(true);
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
        actividad.setActiva(true);
        actividadRepository.save(actividad);
    }

    private ActividadDTO toDTO(Actividad actividad) {
        return new ActividadDTO(
                actividad.getId(),
                actividad.getTitulo(),
                actividad.getDescripcion(),
                actividad.getEje(),
                actividad.getPuntosTdi(),
                actividad.getPeriodicidad(),
                actividad.getFechaInicio(),
                actividad.getFechaFin(),
                actividad.getActiva(),
                actividad.getCreatedAt()
        );
    }
}
