package com.senkiu.orden.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.senkiu.empresa.model.Empresa;
import com.senkiu.entrega.model.Entrega;
import com.senkiu.estado.orden.model.EstadoOrden;
import com.senkiu.usuario.model.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "orden")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "session_id")
    private String sessionId;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private EstadoOrden estado;

    @OneToOne
    @JoinColumn(name = "pago_id")
    private com.senkiu.pago.model.Pago pago;

    @OneToOne(mappedBy = "orden",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private Entrega entrega;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "orden",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrdenDetalle> detalles = new ArrayList<>();

    @Column(name = "email_contacto")
    private String emailContacto;

    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    @Column(name = "direccion_entrega")
    private String direccionEntrega;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public com.senkiu.pago.model.Pago getPago() {
        return pago;
    }

    public void setPago(com.senkiu.pago.model.Pago pago) {
        this.pago = pago;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrdenDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<OrdenDetalle> detalles) {
        this.detalles = detalles;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }
}