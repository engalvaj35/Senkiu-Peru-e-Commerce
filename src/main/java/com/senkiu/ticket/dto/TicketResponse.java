package com.senkiu.ticket.dto;

public class TicketResponse {

    private Long id;
    private String codigo;
    private String rifaTitulo;
    private String estado;
    private Long pagoId;

    public TicketResponse() {
    }

    public TicketResponse(
            Long id,
            String codigo,
            String rifaTitulo,
            String estado,
            Long pagoId
    ) {

        this.id = id;
        this.codigo = codigo;
        this.rifaTitulo = rifaTitulo;
        this.estado = estado;
        this.pagoId = pagoId;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getRifaTitulo() {
        return rifaTitulo;
    }

    public String getEstado() {
        return estado;
    }

    public Long getPagoId() {
        return pagoId;
    }
}