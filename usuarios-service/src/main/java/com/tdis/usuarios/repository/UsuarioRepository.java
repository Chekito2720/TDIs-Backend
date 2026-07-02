package com.tdis.usuarios.repository;

import com.tdis.usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByMatricula(String matricula);
    Optional<Usuario> findByEmail(String email);
    boolean existsByMatricula(String matricula);
    boolean existsByEmail(String email);
    List<Usuario> findByTipoUsuario(com.tdis.common.enums.TipoUsuario tipoUsuario);
}
