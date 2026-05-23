package com.senkiu.solicitud.programa.model;

import java.math.BigDecimal;

import com.senkiu.categoria.model.Categoria;
import com.senkiu.estado.publicacion.model.EstadoPublicacion;
import com.senkiu.ong.model.Ong;

import jakarta.persistence.*;

@Entity
@Table(name = "solicitud_publicacion")
public class SolicitudPrograma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "referencia_id")
    private Long referenciaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ong_id")
    private Ong ong;

    @Column(name = "titulo", length = 150)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // NUEVO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // NUEVO
    @Column(name = "objetivo_monto", precision = 10, scale = 2)
    private BigDecimal objetivoMonto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoPublicacion estado;

    public SolicitudPrograma() {
    }

    @PrePersist
    public void prePersist() {
        if (this.tipo == null || this.tipo.trim().isEmpty()) {
            this.tipo = "PROGRAMA";
        }
    }

    // GETTERS Y SETTERS

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getObjetivoMonto() {
        return objetivoMonto;
    }

    public void setObjetivoMonto(BigDecimal objetivoMonto) {
        this.objetivoMonto = objetivoMonto;
    }

    public Long getId() {
        return id;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public Long getReferenciaId() {
        return referenciaId;
    }
    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }
    public Ong getOng() {
        return ong;
    }
    public void setOng(Ong ong) {
        this.ong = ong;
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
    public EstadoPublicacion getEstado() {
        return estado;
    }
    public void setEstado(EstadoPublicacion estado) {
        this.estado = estado;
    }
}