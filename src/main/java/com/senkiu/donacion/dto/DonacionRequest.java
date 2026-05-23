package com.senkiu.donacion.dto;

import java.math.BigDecimal;

public class DonacionRequest {

    private Long programaId;
    private BigDecimal monto;

    public Long getProgramaId() {
        return programaId;
    }

    public void setProgramaId(Long programaId) {
        this.programaId = programaId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}