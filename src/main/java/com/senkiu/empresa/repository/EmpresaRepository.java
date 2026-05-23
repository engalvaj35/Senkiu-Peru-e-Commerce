package com.senkiu.empresa.repository;

import com.senkiu.empresa.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    List<Empresa> findAll();
    Optional<Empresa> findByUsuarioId(Long usuarioId);

}