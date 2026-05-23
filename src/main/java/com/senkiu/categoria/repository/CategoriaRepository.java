package com.senkiu.categoria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senkiu.categoria.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByTipo(String tipo);

    Optional<Categoria> findByNombre(String nombre);

    Optional<Categoria> findByNombreAndTipo(String nombre, String tipo);
}