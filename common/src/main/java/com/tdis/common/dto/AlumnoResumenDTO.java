package com.tdis.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoResumenDTO {
    private UUID id;
    private String matricula;
    private String nombre;
    private String apellidos;
    private String nivel;
    private int personal;
    private int social;
    private int dep;
    private int trasc;
    private int total;
    private String tutor;
    private LocalDateTime createdAt;
}
