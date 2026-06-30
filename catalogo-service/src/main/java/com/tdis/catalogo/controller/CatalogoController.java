package com.tdis.catalogo.controller;

import com.tdis.common.dto.ActividadDTO;
import com.tdis.common.enums.EjeFormativo;
import com.tdis.catalogo.service.CatalogoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogo")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService catalogoService;

    @GetMapping
    public ResponseEntity<List<ActividadDTO>> listar(@RequestParam(required = false) Boolean todas) {
        if (Boolean.TRUE.equals(todas)) {
            return ResponseEntity.ok(catalogoService.listarTodas());
        }
        return ResponseEntity.ok(catalogoService.listarActivas());
    }

    @GetMapping("/eje/{eje}")
    public ResponseEntity<List<ActividadDTO>> listarPorEje(@PathVariable EjeFormativo eje) {
        return ResponseEntity.ok(catalogoService.listarPorEje(eje));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadDTO> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(catalogoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ActividadDTO> crear(@Valid @RequestBody ActividadDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(catalogoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadDTO> actualizar(@PathVariable UUID id,
                                                    @Valid @RequestBody ActividadDTO dto) {
        return ResponseEntity.ok(catalogoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable UUID id) {
        catalogoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable UUID id) {
        catalogoService.activar(id);
        return ResponseEntity.ok().build();
    }
}
