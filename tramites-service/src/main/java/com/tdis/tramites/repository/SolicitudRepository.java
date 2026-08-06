package com.tdis.tramites.repository;

import com.tdis.common.enums.EstadoSolicitud;
import com.tdis.tramites.entity.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;


public interface SolicitudRepository extends JpaRepository<Solicitud, UUID> {
    List<Solicitud> findByAlumnoIdOrderByCreatedAtDesc(UUID alumnoId);
    List<Solicitud> findByEstadoOrderByCreatedAtDesc(EstadoSolicitud estado);
    List<Solicitud> findByAlumnoIdAndEstado(UUID alumnoId, EstadoSolicitud estado);
    long countByAlumnoIdAndEstado(UUID alumnoId, EstadoSolicitud estado);
    boolean existsByAlumnoIdAndActividadIdAndEstado(UUID alumnoId, UUID actividadId, EstadoSolicitud estado);
}
