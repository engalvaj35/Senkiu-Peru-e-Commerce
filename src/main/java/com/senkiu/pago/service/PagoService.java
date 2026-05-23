package com.senkiu.pago.service;

import com.senkiu.pago.dto.CrearPagoRequest;
import com.senkiu.pago.model.Pago;

public interface PagoService {

    Pago crearPago(CrearPagoRequest request);

    Pago confirmarPago(Long pagoId);
}