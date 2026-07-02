package com.tdis.usuarios.controller;

import com.tdis.common.dto.AdminResumenDTO;
import com.tdis.common.dto.AlumnoResumenDTO;
import com.tdis.usuarios.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/resumen")
    public ResponseEntity<AdminResumenDTO> obtenerResumen() {
        return ResponseEntity.ok(adminService.obtenerResumen());
    }

    @GetMapping("/alumnos")
    public ResponseEntity<List<AlumnoResumenDTO>> listarAlumnos() {
        return ResponseEntity.ok(adminService.listarAlumnos());
    }
}
