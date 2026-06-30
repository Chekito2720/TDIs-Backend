package com.tdis.common.dto;

import com.tdis.common.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UUID usuarioId;
    private String nombre;
    private String apellidos;
    private TipoUsuario tipoUsuario;
}
