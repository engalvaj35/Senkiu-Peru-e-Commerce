package com.senkiu.rifa.dto;

import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RifaResponse {

    private Long id;

    private String titulo;

    private String descripcion;

    private String programaNombre;

    private String ongNombre;

    private BigDecimal precioTicket;

    private Integer stockTickets;

    private Integer ticketsVendidos;

    private String imagenUrl;

    private LocalDateTime fechaFin;

    private String estado;

    private Double progresoPrograma;

    private Double progresoRifa;

    private Integer empresasAsociadas;

    private List<RifaEmpresaResponse> empresas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getProgramaNombre() {
        return programaNombre;
    }

    public void setProgramaNombre(String programaNombre) {
        this.programaNombre = programaNombre;
    }

    public String getOngNombre() {
        return ongNombre;
    }

    public void setOngNombre(String ongNombre) {
        this.ongNombre = ongNombre;
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

    public Integer getTicketsVendidos() {
        return ticketsVendidos;
    }

    public void setTicketsVendidos(Integer ticketsVendidos) {
        this.ticketsVendidos = ticketsVendidos;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getProgresoPrograma() {
        return progresoPrograma;
    }

    public void setProgresoPrograma(Double progresoPrograma) {
        this.progresoPrograma = progresoPrograma;
    }

    public Double getProgresoRifa() {
        return progresoRifa;
    }

    public void setProgresoRifa(Double progresoRifa) {
        this.progresoRifa = progresoRifa;
    }

    public Integer getEmpresasAsociadas() {
    return empresasAsociadas;
}

    public void setEmpresasAsociadas(Integer empresasAsociadas) {
        this.empresasAsociadas = empresasAsociadas;
    }

    public List<RifaEmpresaResponse> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<RifaEmpresaResponse> empresas) {
        this.empresas = empresas;
    }
}