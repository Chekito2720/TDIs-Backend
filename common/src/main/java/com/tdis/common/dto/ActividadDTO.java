package com.tdis.common.dto;

import com.tdis.common.enums.AsignaturaFormacion;
import com.tdis.common.enums.CompetenciaReforzada;
import com.tdis.common.enums.DimensionFormacion;
import com.tdis.common.enums.EjeFormativo;
import com.tdis.common.enums.EstadoRevision;
import com.tdis.common.enums.NivelImpacto;
import com.tdis.common.enums.Periodicidad;
import com.tdis.common.enums.PublicoObjetivo;
import com.tdis.common.enums.TipoEvidenciaRequerida;
import com.tdis.common.enums.TipoLugar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActividadDTO {
    private UUID id;
    private String titulo;
    private String descripcion;
    private EjeFormativo eje;
    private Integer puntosTdi;
    private Periodicidad periodicidad;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer horasEfectivas;
    private TipoLugar lugar;
    private List<DimensionFormacion> dimensionesFormacion;
    private NivelImpacto nivelImpacto;
    private List<PublicoObjetivo> publicoObjetivo;
    private List<AsignaturaFormacion> asignaturasRelacionadas;
    private List<CompetenciaReforzada> competenciasReforzar;
    private List<TipoEvidenciaRequerida> tiposEvidenciaRequerida;
    private Boolean activa;
    private EstadoRevision estadoRevision;
    private UUID creadorId;
    private String creadorNombre;
    private String creadorTipo;
    private String area;
    private String comentarioRevision;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}