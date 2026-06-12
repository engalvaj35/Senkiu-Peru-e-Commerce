package com.senkiu.donacion.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.senkiu.pago.model.Pago;
import com.senkiu.programa.model.ProgramaSocial;
import com.senkiu.usuario.model.Usuario;

import jakarta.persistence.*;

@Entity
@Table(name = "donacion")
public class Donacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "programa_id")
    private ProgramaSocial programa;

    private BigDecimal monto;

    @OneToOne
    @JoinColumn(name = "pago_id")
    private Pago pago;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
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

    public ProgramaSocial getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaSocial programa) {
        this.programa = programa;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}