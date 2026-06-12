package com.senkiu.orden.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.senkiu.orden.model.Orden;

public interface OrdenRepository
        extends JpaRepository<Orden, Long> {
        @Query("""
        SELECT DISTINCT o
        FROM Orden o
        LEFT JOIN FETCH o.entrega e
        LEFT JOIN FETCH e.estado
        LEFT JOIN FETCH o.detalles d
        LEFT JOIN FETCH d.producto
        WHERE o.usuario.id = :usuarioId
    """)

    List<Orden> findByUsuarioId(Long usuarioId);

    List<Orden> findByEmpresaId(Long empresaId);

    Optional<Orden> findByPagoId(Long pagoId);
}