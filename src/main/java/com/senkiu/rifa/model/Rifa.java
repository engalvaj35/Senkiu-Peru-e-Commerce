package com.senkiu.rifa.model;

import com.senkiu.estado.rifa.model.EstadoRifa;
import com.senkiu.programa.model.ProgramaSocial;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rifa")
public class Rifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "programa_id")
    private ProgramaSocial programa;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String imagenUrl;

    private BigDecimal precioTicket;

    private Integer stockTickets;

    private Integer ticketsVendidos = 0;

    private LocalDateTime fechaFin;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private EstadoRifa estado;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProgramaSocial getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaSocial programa) {
        this.programa = programa;
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

    public Integer getTicketsVendidos() {
        return ticketsVendidos;
    }

    public void setTicketsVendidos(Integer ticketsVendidos) {
        this.ticketsVendidos = ticketsVendidos;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoRifa getEstado() {
        return estado;
    }

    public void setEstado(EstadoRifa estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}