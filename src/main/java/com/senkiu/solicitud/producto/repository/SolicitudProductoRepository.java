package com.senkiu.solicitud.producto.repository;

import com.senkiu.solicitud.producto.model.SolicitudProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudProductoRepository
        extends JpaRepository<SolicitudProducto, Long> {

    List<SolicitudProducto> findByEstadoNombre(String nombre);

    List<SolicitudProducto> findByTipo(String tipo);
}