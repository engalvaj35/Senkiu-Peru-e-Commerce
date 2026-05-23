package com.senkiu.estado.rifa.repository;

import com.senkiu.estado.rifa.model.EstadoRifa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoRifaRepository extends JpaRepository<EstadoRifa, Long> {

    Optional<EstadoRifa> findByNombre(String nombre);

}