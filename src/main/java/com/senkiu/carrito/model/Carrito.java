package com.senkiu.carrito.model;

import com.senkiu.usuario.model.Usuario;
import com.senkiu.empresa.model.Empresa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // USUARIO
    // =========================

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // =========================
    // EMPRESA
    // =========================

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    // =========================
    // ITEMS
    // =========================

    @OneToMany(
            mappedBy = "carrito",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CarritoItem> items = new ArrayList<>();

    // =========================
    // FECHA
    // =========================

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<CarritoItem> getItems() {
        return items;
    }

    public void setItems(List<CarritoItem> items) {
        this.items = items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}