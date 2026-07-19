package com.tdis.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterExternoRequest {

    @NotBlank(message = "El tipo es requerido")
    private String tipo; // PERSONA u ORGANIZACION

    private String nombre;

    private String apellidos;

    @NotBlank(message = "El correo electronico es requerido")
    @Email(message = "El correo debe ser valido")
    private String email;

    @NotBlank(message = "La contrasena es requerida")
    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    private String password;
}
