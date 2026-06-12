package com.senkiu.pago.repository;

import com.senkiu.pago.model.Pago;
import com.senkiu.pago.model.TipoPago;

import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

        Optional<Pago> findByReferenciaIdAndTipo(
                Long referenciaId,
                TipoPago tipo
        );

        @Query("""
        SELECT COALESCE(SUM(p.monto), 0)
        FROM Pago p
        WHERE p.usuario.id = :usuarioId
        AND p.tipo = com.senkiu.pago.model.TipoPago.RECARGA_WALLET
        AND p.estado.nombre = 'COMPLETADO'
        AND p.createdAt >= :inicioMes
        AND p.createdAt < :finMes
        """)
        BigDecimal obtenerTotalRecargadoMes(
                @Param("usuarioId") Long usuarioId,
                @Param("inicioMes") java.time.LocalDateTime inicioMes,
                @Param("finMes") java.time.LocalDateTime finMes
        );
}