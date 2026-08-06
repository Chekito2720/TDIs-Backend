package com.tdis.catalogo.entity;

import com.tdis.common.enums.EjeFormativo;
import com.tdis.common.enums.EstadoRevision;
import com.tdis.common.enums.Periodicidad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "actividades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EjeFormativo eje;

    @Column(name = "puntos_tdi", nullable = false)
    private Integer puntosTdi;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Periodicidad periodicidad;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(nullable = false)
    private Boolean activa = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_revision", nullable = false, length = 20)
    private EstadoRevision estadoRevision = EstadoRevision.PENDIENTE;

    @Column(name = "creador_id")
    private UUID creadorId;

    @Column(name = "creador_tipo", length = 20)
    private String creadorTipo;

    @Column(name = "comentario_revision", columnDefinition = "TEXT")
    private String comentarioRevision;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (estadoRevision == null) estadoRevision = EstadoRevision.PENDIENTE;
        if (activa == null) activa = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
