package com.senkiu.certificado.model;

import com.senkiu.empresa.model.Empresa;
import com.senkiu.solicitud.model.SolicitudOrganizacion;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "certificado_organizacion")
public class CertificadoOrganizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(name = "archivo_url")
    private String archivoUrl;

    @ManyToOne
    @JoinColumn(name = "solicitud_id")
    private SolicitudOrganizacion solicitud;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArchivoUrl() {
        return archivoUrl;
    }

    public void setArchivoUrl(String archivoUrl) {
        this.archivoUrl = archivoUrl;
    }

    public SolicitudOrganizacion getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudOrganizacion solicitud) {
        this.solicitud = solicitud;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}