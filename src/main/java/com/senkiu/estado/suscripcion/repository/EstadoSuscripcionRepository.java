package com.senkiu.estado.suscripcion.repository;

import com.senkiu.estado.suscripcion.model.EstadoSuscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoSuscripcionRepository extends JpaRepository<EstadoSuscripcion, Long> {

    Optional<EstadoSuscripcion> findByNombre(String nombre);
}