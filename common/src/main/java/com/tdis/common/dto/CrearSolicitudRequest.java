package com.tdis.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearSolicitudRequest {
    @NotNull(message = "El ID de la actividad es requerido")
    private UUID actividadId;

    private String tipoSolicitud;
    private String descripcion;
    private String reflexion;
    private String lugar;
    private String horas;
    private String tipoActividad;
    private String materiaRelacionada;
    private String division;
    private String programa;
    private String grupo;
    private String cuatrimestre;
    private String turno;
    private String tutor;
    private String nombreResponsable;
    private String cargoResponsable;
    private String telefonoResponsable;
    private String correoResponsable;
}
