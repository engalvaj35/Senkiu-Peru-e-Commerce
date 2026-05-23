package com.senkiu.rifa.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CrearRifaRequest {

    private Long programaId;

    private String titulo;

    private String descripcion;

    private String imagenUrl;

    private BigDecimal precioTicket;

    private Integer stockTickets;

    private LocalDateTime fechaFin;

    // GETTERS & SETTERS

    public Long getProgramaId() {
        return programaId;
    }

    public void setProgramaId(Long programaId) {
        this.programaId = programaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public BigDecimal getPrecioTicket() {
        return precioTicket;
    }

    public void setPrecioTicket(BigDecimal precioTicket) {
        this.precioTicket = precioTicket;
    }

    public Integer getStockTickets() {
        return stockTickets;
    }

    public void setStockTickets(Integer stockTickets) {
        this.stockTickets = stockTickets;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }
}