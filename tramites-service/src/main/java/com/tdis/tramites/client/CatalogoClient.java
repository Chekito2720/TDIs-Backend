package com.tdis.tramites.client;

import com.tdis.common.dto.ActividadDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "catalogo-service")
public interface CatalogoClient {

    @GetMapping("/api/catalogo/{id}")
    ActividadDTO obtenerActividad(@PathVariable("id") UUID id);
}
