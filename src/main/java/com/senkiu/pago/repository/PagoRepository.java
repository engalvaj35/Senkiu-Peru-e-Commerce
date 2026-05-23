package com.senkiu.pago.repository;

import com.senkiu.pago.model.Pago;
import com.senkiu.pago.model.TipoPago;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByReferenciaIdAndTipo(
                Long referenciaId,
                TipoPago tipo
        );
}