package com.senkiu.pago.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.senkiu.estado.pago.model.EstadoPago;
import com.senkiu.usuario.model.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import com.senkiu.ticket.model.Ticket;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private TipoPago tipo;

    @Column(name = "referencia_id")
    private Long referenciaId;

    private BigDecimal monto;

    private BigDecimal comisionSenkiu;

    private BigDecimal montoNeto;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoPago estado;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodo;

    private String culqiChargeId;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.comisionSenkiu = BigDecimal.ZERO;
        this.montoNeto = BigDecimal.ZERO;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "pago")
    private List<Ticket> tickets;

    // GETTERS Y SETTERS

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoPago getTipo() {
        return tipo;
    }

    public void setTipo(TipoPago tipo) {
        this.tipo = tipo;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getComisionSenkiu() {
        return comisionSenkiu;
    }

    public void setComisionSenkiu(BigDecimal comisionSenkiu) {
        this.comisionSenkiu = comisionSenkiu;
    }

    public BigDecimal getMontoNeto() {
        return montoNeto;
    }

    public void setMontoNeto(BigDecimal montoNeto) {
        this.montoNeto = montoNeto;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public MetodoPago getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPago metodo) {
        this.metodo = metodo;
    }

    public String getCulqiChargeId() {
        return culqiChargeId;
    }

    public void setCulqiChargeId(String culqiChargeId) {
        this.culqiChargeId = culqiChargeId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}