package com.senkiu.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.senkiu.producto.model.Producto;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByEmpresaId(Long empresaId);

    List<Producto> findByEstadoNombre(String nombre);

    List<Producto> findByEstadoNombreAndNombreContainingIgnoreCase(
            String estado,
            String nombre
    );

    List<Producto> findByEstadoNombreAndCategoriaNombre(
            String estado,
            String categoria
    );

    List<Producto> findByEstadoNombreAndNombreContainingIgnoreCaseAndCategoriaNombre(
            String estado,
            String nombre,
            String categoria
    );
}