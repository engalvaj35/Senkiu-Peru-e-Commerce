package com.senkiu.estado.publicacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senkiu.estado.publicacion.model.EstadoPublicacion;

public interface EstadoPublicacionRepository extends JpaRepository<EstadoPublicacion, Long> {

    Optional<EstadoPublicacion> findByNombre(String nombre);
}