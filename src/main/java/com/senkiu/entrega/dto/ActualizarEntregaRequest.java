package com.senkiu.entrega.dto;

public class ActualizarEntregaRequest {

    private String estado;

    private String comentario;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}