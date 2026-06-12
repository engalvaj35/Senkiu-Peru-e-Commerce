package com.senkiu.estado.organizacion.repository;

import com.senkiu.estado.organizacion.model.EstadoOrganizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoOrganizacionRepository extends JpaRepository<EstadoOrganizacion, Long> {

    Optional<EstadoOrganizacion> findByNombre(String nombre);
}