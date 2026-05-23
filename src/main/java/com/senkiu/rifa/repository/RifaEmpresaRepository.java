package com.senkiu.rifa.repository;

import com.senkiu.rifa.model.RifaEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RifaEmpresaRepository extends JpaRepository<RifaEmpresa, Long> {

    List<RifaEmpresa> findByRifaId(Long rifaId);

    List<RifaEmpresa> findByEmpresaId(Long empresaId);

    boolean existsByRifaIdAndProductoId(Long rifaId, Long productoId);
}