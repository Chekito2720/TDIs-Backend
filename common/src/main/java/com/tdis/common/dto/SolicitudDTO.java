package com.tdis.common.dto;

import com.tdis.common.enums.EstadoSolicitud;
import com.tdis.common.enums.EjeFormativo;
import com.tdis.common.enums.TipoLugar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {
    private UUID id;
    private UUID alumnoId;
    private String alumnoMatricula;
    private String alumnoNombre;
    private String tipoSolicitud;
    private UUID actividadId;
    private String nombreActividad;
    private String actividadTitulo;
    private String actividadEje;
    private Integer actividadPuntos;
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
    private String nombreArchivo;
    private EstadoSolicitud estado;
    private String comentarioRechazo;
    private String archivoPath;
    private String aiEstado;
    private String aiMotivo;
    private String aiDescripcionAnalisis;
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
    private String periodicidad;
    private String fechaInicio;
    private String fechaFin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Campos de Actividad para PREVIA
    private EjeFormativo eje;
    private Integer puntosTdi;
    private Integer horasEfectivas;
    private TipoLugar tipoLugar;
}