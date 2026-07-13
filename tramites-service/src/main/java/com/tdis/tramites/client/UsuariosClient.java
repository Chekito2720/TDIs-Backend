package com.tdis.tramites.client;

import com.tdis.common.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "usuarios-service")
public interface UsuariosClient {

    @GetMapping("/api/usuarios/{id}")
    UsuarioDTO obtenerPorId(@PathVariable("id") UUID id);
}
