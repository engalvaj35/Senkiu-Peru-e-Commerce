package com.senkiu.pago.dto;

import com.senkiu.pago.model.MetodoPago;
import com.senkiu.pago.model.TipoPago;

import java.math.BigDecimal;

public class CrearPagoRequest {

    private Long usuarioId;

    private TipoPago tipo;

    private Long referenciaId;

    private BigDecimal monto;

    private MetodoPago metodo;

    // GETTERS Y SETTERS

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public TipoPago getTipo() { return tipo; }
    public void setTipo(TipoPago tipo) { this.tipo = tipo; }

    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public MetodoPago getMetodo() { return metodo; }
    public void setMetodo(MetodoPago metodo) { this.metodo = metodo; }
}