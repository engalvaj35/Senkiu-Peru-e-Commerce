package com.senkiu.entrega.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senkiu.entrega.model.Entrega;

public interface EntregaRepository
        extends JpaRepository<Entrega, Long> {

    Optional<Entrega> findByOrdenId(Long ordenId);
}