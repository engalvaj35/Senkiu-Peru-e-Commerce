package com.senkiu.orden.service;

import java.util.List;

import com.senkiu.orden.model.Orden;

public interface OrdenService {

    Orden crearOrden(
            Long usuarioId,
            Long empresaId
    );

    List<Orden> listarPorUsuario(
            Long usuarioId
    );

    List<Orden> listarPorEmpresa(
            Long empresaId
    );

    void cambiarEstado(
            Long ordenId,
            String estado
    );
}