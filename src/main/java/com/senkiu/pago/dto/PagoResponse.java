package com.senkiu.pago.dto;

import com.senkiu.pago.model.TipoPago;

import java.math.BigDecimal;

public class PagoResponse {

    private Long id;
    private Long usuarioId;
    private TipoPago tipo;
    private Long referenciaId;
    private BigDecimal monto;
    private String estado;
    private String metodo;

    // GETTERS Y SETTERS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public TipoPago getTipo() { return tipo; }
    public void setTipo(TipoPago tipo) { this.tipo = tipo; }

    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
}