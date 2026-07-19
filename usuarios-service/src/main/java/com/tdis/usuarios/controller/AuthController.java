package com.tdis.usuarios.controller;

import com.tdis.common.dto.LoginRequest;
import com.tdis.common.dto.LoginResponse;
import com.tdis.common.dto.RegisterRequest;
import com.tdis.common.dto.RegisterExternoRequest;
import com.tdis.common.exception.BadRequestException;
import com.tdis.common.exception.UnauthorizedException;
import com.tdis.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = usuarioService.login(request);
            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                java.util.Map.of("message", e.getMessage())
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            LoginResponse response = usuarioService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(
                java.util.Map.of("message", e.getMessage())
            );
        }
    }

    @PostMapping("/register-externo")
    public ResponseEntity<?> registerExterno(@Valid @RequestBody RegisterExternoRequest request) {
        try {
            LoginResponse response = usuarioService.registerExterno(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(
                java.util.Map.of("message", e.getMessage())
            );
        }
    }
}
