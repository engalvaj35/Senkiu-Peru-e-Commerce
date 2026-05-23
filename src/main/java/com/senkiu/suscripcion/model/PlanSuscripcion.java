package com.senkiu.suscripcion.model;

import java.math.BigDecimal;

public enum PlanSuscripcion {

    BASIC(new BigDecimal("9.90")),
    PRO(new BigDecimal("29.90")),
    PREMIUM(new BigDecimal("79.90"));

    private final BigDecimal precio;

    PlanSuscripcion(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public static boolean existe(String plan) {
        try {
            valueOf(plan);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}