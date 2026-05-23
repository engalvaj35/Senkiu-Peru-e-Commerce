package com.senkiu.rifa.ticket.dto;

import java.time.LocalDateTime;

public class TicketRifaDetalleResponse {

    private Long ticketId;
    private Long rifaId;
    private String rifaTitulo;

    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioEmail;

    private LocalDateTime fechaCompra;

    private String estado;

    public TicketRifaDetalleResponse() {
    }

    public TicketRifaDetalleResponse(Long ticketId,
                                     Long rifaId,
                                     String rifaTitulo,
                                     Long usuarioId,
                                     String usuarioNombre,
                                     String usuarioEmail,
                                     LocalDateTime fechaCompra,
                                     String estado) {
        this.ticketId = ticketId;
        this.rifaId = rifaId;
        this.rifaTitulo = rifaTitulo;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.usuarioEmail = usuarioEmail;
        this.fechaCompra = fechaCompra;
        this.estado = estado;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getRifaId() {
        return rifaId;
    }

    public void setRifaId(Long rifaId) {
        this.rifaId = rifaId;
    }

    public String getRifaTitulo() {
        return rifaTitulo;
    }

    public void setRifaTitulo(String rifaTitulo) {
        this.rifaTitulo = rifaTitulo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}