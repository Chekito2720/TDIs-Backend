package com.tdis.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearSolicitudRequest {
    private UUID actividadId;
    private String nombreActividad;

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

    // Campos específicos de Solicitud Previa
    private String dimensionesFormacion;
    private String nivelImpacto;
    private String publicoObjetivo;
    private String asignaturasRelacionadas;
    private String competenciasReforzar;
    private String evidenciasRequeridas;
    private String justificacionPersonal;
    private String impactoAcademico;
    private String asistenciaEsperada;
    private String alumnosGeneranTdi;
    private String horasEstimadas;
}
