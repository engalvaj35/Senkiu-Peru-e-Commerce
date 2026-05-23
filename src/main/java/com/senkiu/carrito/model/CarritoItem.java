package com.senkiu.carrito.model;

import com.senkiu.producto.model.Producto;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "carrito_item",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "carrito_id",
                                "producto_id"
                        }
                )
        }
)
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // CARRITO
    // =========================

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    // =========================
    // PRODUCTO
    // =========================

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    // =========================
    // CANTIDAD
    // =========================

    private Integer cantidad;

    // =========================
    // PRECIO CONGELADO
    // =========================

    private BigDecimal precio;

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

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}