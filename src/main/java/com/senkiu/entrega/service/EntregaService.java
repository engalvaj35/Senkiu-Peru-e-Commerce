package com.senkiu.entrega.service;

import com.senkiu.entrega.dto.ActualizarEntregaRequest;
import com.senkiu.entrega.dto.EntregaResponse;

public interface EntregaService {

    EntregaResponse obtenerPorOrden(Long ordenId);

    EntregaResponse actualizarEstado(
            Long ordenId,
            ActualizarEntregaRequest request
    );

    void crearEntregaInicial(Long ordenId);
}