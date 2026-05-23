package com.senkiu.donacion.repository;

import com.senkiu.donacion.model.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DonacionRepository extends JpaRepository<Donacion, Long> {
    List<Donacion> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    List<Donacion> findAllByOrderByCreatedAtDesc();

    Optional<Donacion> findByPagoId(Long pagoId);
}