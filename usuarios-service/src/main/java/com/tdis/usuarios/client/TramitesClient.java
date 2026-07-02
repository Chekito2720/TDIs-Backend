package com.tdis.usuarios.client;

import com.tdis.common.dto.SolicitudDTO;
import com.tdis.common.enums.EstadoSolicitud;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "tramites-service")
public interface TramitesClient {

    @GetMapping("/api/solicitudes")
    List<SolicitudDTO> listarTodas();

    @GetMapping("/api/solicitudes/alumno/{alumnoId}")
    List<SolicitudDTO> listarPorAlumno(@PathVariable("alumnoId") UUID alumnoId);
}
