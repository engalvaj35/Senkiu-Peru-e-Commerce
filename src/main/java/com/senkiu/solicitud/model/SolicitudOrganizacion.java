package com.senkiu.solicitud.model;

import com.senkiu.usuario.model.Usuario;
import com.senkiu.estado.organizacion.model.EstadoOrganizacion;
import com.senkiu.certificado.model.CertificadoOrganizacion;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "solicitud_organizacion")
public class SolicitudOrganizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String nombre;
    private String ruc;
    private String email;
    private String direccion;

    @Column(columnDefinition = "JSON")
    private String datosJson;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private EstadoOrganizacion estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "solicitud")
    private List<CertificadoOrganizacion> certificados;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDatosJson() {
        return datosJson;
    }

    public void setDatosJson(String datosJson) {
        this.datosJson = datosJson;
    }

    public EstadoOrganizacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrganizacion estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<CertificadoOrganizacion> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<CertificadoOrganizacion> certificados) {
        this.certificados = certificados;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}