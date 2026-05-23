package com.senkiu.ticket.dto;

public class CompraTicketResponse {

    private Long pagoId;

    public CompraTicketResponse() {
    }

    public CompraTicketResponse(Long pagoId) {
        this.pagoId = pagoId;
    }

    public Long getPagoId() {
        return pagoId;
    }

    public void setPagoId(Long pagoId) {
        this.pagoId = pagoId;
    }
}