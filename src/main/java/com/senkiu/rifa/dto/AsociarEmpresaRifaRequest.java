package com.senkiu.rifa.dto;

public class AsociarEmpresaRifaRequest {

    private Long rifaId;

    private Long productoId;

    // GETTERS & SETTERS

    public Long getRifaId() {
        return rifaId;
    }

    public void setRifaId(Long rifaId) {
        this.rifaId = rifaId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
}