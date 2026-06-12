package com.senkiu.solicitud.repository;

import com.senkiu.solicitud.model.SolicitudOrganizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudOrganizacionRepository
        extends JpaRepository<SolicitudOrganizacion, Long> {

    List<SolicitudOrganizacion> findByEstadoNombre(String nombre);
}