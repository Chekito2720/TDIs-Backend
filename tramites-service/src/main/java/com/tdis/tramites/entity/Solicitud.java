package com.tdis.tramites.entity;

import com.tdis.common.enums.EstadoSolicitud;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitudes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "alumno_id", nullable = false)
    private UUID alumnoId;

    @Column(name = "tipo_solicitud", length = 20)
    private String tipoSolicitud;

    @Column(name = "actividad_id", nullable = false)
    private UUID actividadId;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String reflexion;

    @Column(length = 100)
    private String lugar;

    @Column(length = 50)
    private String horas;

    @Column(name = "tipo_actividad", length = 100)
    private String tipoActividad;

    @Column(name = "materia_relacionada", length = 200)
    private String materiaRelacionada;

    @Column(length = 100)
    private String division;

    @Column(length = 100)
    private String programa;

    @Column(length = 50)
    private String grupo;

    @Column(length = 20)
    private String cuatrimestre;

    @Column(length = 20)
    private String turno;

    @Column(length = 200)
    private String tutor;

    @Column(name = "nombre_responsable", length = 200)
    private String nombreResponsable;

    @Column(name = "cargo_responsable", length = 200)
    private String cargoResponsable;

    @Column(name = "telefono_responsable", length = 50)
    private String telefonoResponsable;

    @Column(name = "correo_responsable", length = 200)
    private String correoResponsable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSolicitud estado = EstadoSolicitud.EN_REVISION;

    @Column(columnDefinition = "TEXT")
    private String comentarioRechazo;

    @Column(name = "archivo_path")
    private String archivoPath;

    @Column(name = "nombre_archivo", length = 255)
    private String nombreArchivo;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
