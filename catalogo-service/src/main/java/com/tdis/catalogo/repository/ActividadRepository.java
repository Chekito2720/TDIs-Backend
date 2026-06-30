package com.tdis.catalogo.repository;

import com.tdis.catalogo.entity.Actividad;
import com.tdis.common.enums.EjeFormativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, UUID> {
    List<Actividad> findByEje(EjeFormativo eje);
    List<Actividad> findByActivaTrue();
    List<Actividad> findByEjeAndActivaTrue(EjeFormativo eje);
}
