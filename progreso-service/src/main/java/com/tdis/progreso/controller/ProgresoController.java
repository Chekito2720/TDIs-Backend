package com.tdis.progreso.controller;

import com.tdis.common.dto.ProgresoDTO;
import com.tdis.progreso.service.ProgresoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/progreso")
@RequiredArgsConstructor
public class ProgresoController {

    private final ProgresoService progresoService;

    @GetMapping("/{alumnoId}")
    public ResponseEntity<ProgresoDTO> obtenerProgreso(@PathVariable UUID alumnoId) {
        return ResponseEntity.ok(progresoService.calcularProgreso(alumnoId));
    }
}
