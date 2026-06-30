package com.tdis.usuarios.service;

import com.tdis.common.dto.LoginRequest;
import com.tdis.common.dto.LoginResponse;
import com.tdis.common.dto.UsuarioDTO;
import com.tdis.common.enums.TipoUsuario;
import com.tdis.common.exception.BadRequestException;
import com.tdis.common.exception.ResourceNotFoundException;
import com.tdis.common.exception.UnauthorizedException;
import com.tdis.usuarios.entity.Usuario;
import com.tdis.usuarios.repository.UsuarioRepository;
import com.tdis.usuarios.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        String credencial = request.getCredencial();

        if (credencial.matches("\\d{8,}")) {
            Usuario alumno = usuarioRepository.findByMatricula(credencial)
                    .orElseThrow(() -> new UnauthorizedException("Matricula no registrada"));

            if (!alumno.getActivo()) {
                throw new UnauthorizedException("Usuario inactivo");
            }

            String token = jwtTokenProvider.generateToken(alumno.getId(), alumno.getTipoUsuario());
            return new LoginResponse(token, alumno.getId(), alumno.getNombre(), alumno.getApellidos(), alumno.getTipoUsuario());
        }

        Usuario admin = usuarioRepository.findByEmail(credencial)
                .orElseThrow(() -> new UnauthorizedException("Credenciales invalidas"));

        if (request.getPassword() == null || !passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new UnauthorizedException("Credenciales invalidas");
        }

        if (!admin.getActivo()) {
            throw new UnauthorizedException("Usuario inactivo");
        }

        String token = jwtTokenProvider.generateToken(admin.getId(), admin.getTipoUsuario());
        return new LoginResponse(token, admin.getId(), admin.getNombre(), admin.getApellidos(), admin.getTipoUsuario());
    }

    public UsuarioDTO crearUsuario(UsuarioDTO dto, String password) {
        if (dto.getTipoUsuario() == TipoUsuario.ALUMNO) {
            if (dto.getMatricula() == null || dto.getMatricula().length() < 8) {
                throw new BadRequestException("La matricula debe tener al menos 8 digitos");
            }
            if (usuarioRepository.existsByMatricula(dto.getMatricula())) {
                throw new BadRequestException("La matricula ya esta registrada");
            }
        } else {
            if (dto.getEmail() == null || usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new BadRequestException("El email ya esta registrado");
            }
        }

        Usuario usuario = new Usuario();
        usuario.setMatricula(dto.getMatricula());
        usuario.setEmail(dto.getEmail());
        usuario.setNombre(dto.getNombre());
        usuario.setApellidos(dto.getApellidos());
        usuario.setTipoUsuario(dto.getTipoUsuario());
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setActivo(true);

        usuario = usuarioRepository.save(usuario);
        return toDTO(usuario);
    }

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO obtenerPorId(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return toDTO(usuario);
    }

    public UsuarioDTO obtenerPorMatricula(String matricula) {
        Usuario usuario = usuarioRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return toDTO(usuario);
    }

    public void desactivarUsuario(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getMatricula(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getTipoUsuario(),
                usuario.getActivo()
        );
    }
}
