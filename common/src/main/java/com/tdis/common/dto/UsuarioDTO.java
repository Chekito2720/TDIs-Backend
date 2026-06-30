package com.tdis.common.dto;

import com.tdis.common.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private UUID id;
    private String matricula;
    private String email;
    private String nombre;
    private String apellidos;
    private TipoUsuario tipoUsuario;
    private Boolean activo;
}
