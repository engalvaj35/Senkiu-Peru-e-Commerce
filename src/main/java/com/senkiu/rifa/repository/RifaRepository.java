package com.senkiu.rifa.repository;

import com.senkiu.rifa.model.Rifa;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RifaRepository extends JpaRepository<Rifa, Long> {

    List<Rifa> findByEstadoNombre(String nombre);

    List<Rifa> findByProgramaId(Long programaId);

    @Query("""
        SELECT r
        FROM Rifa r
        WHERE r.programa.ong.id = :ongId
    """)
    List<Rifa> findByOngId(@Param("ongId") Long ongId);

    @Query("""
        SELECT r
        FROM Rifa r
        WHERE r.estado.nombre = 'ACTIVA'
        AND r.fechaFin > CURRENT_TIMESTAMP
    """)
    List<Rifa> findRifasActivasVigentes();

    @Query("""
        SELECT r
        FROM Rifa r
        WHERE r.estado.nombre = 'ACTIVA'
        AND r.fechaFin > CURRENT_TIMESTAMP
        AND LOWER(r.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))
    """)
    List<Rifa> findRifasActivasVigentesByTitulo(
            @Param("titulo") String titulo
    );
}