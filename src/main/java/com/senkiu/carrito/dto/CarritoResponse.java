package com.senkiu.carrito.dto;

import java.math.BigDecimal;
import java.util.List;

public class CarritoResponse {

    private Long carritoId;

    private Long empresaId;

    private String empresa;

    private List<CarritoItemResponse> items;

    private BigDecimal total;

    public Long getCarritoId() {
        return carritoId;
    }

    public void setCarritoId(Long carritoId) {
        this.carritoId = carritoId;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public List<CarritoItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CarritoItemResponse> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}