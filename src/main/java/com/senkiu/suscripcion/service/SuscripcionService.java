package com.senkiu.suscripcion.service;

import com.senkiu.suscripcion.model.Suscripcion;

public interface SuscripcionService {
    Suscripcion crearSuscripcion(Long usuarioId, String plan);
    Suscripcion obtenerPorUsuario(Long usuarioId);
}
