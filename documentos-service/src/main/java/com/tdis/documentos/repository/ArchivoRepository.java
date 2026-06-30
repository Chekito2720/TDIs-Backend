package com.tdis.documentos.repository;

import com.tdis.documentos.entity.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, UUID> {
    Optional<Archivo> findBySolicitudId(UUID solicitudId);
    boolean existsBySolicitudId(UUID solicitudId);
    void deleteBySolicitudId(UUID solicitudId);
}
