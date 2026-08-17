package com.tdis.catalogo.client;

import com.tdis.common.dto.SolicitudDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "tramites-service")
public interface TramitesClient {

    @GetMapping("/api/solicitudes/{id}")
    SolicitudDTO obtenerSolicitud(@PathVariable UUID id);
}