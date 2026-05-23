package com.senkiu.ong.service;

import com.senkiu.ong.model.Ong;
import com.senkiu.solicitud.model.SolicitudOrganizacion;

import java.util.List;

public interface OngService {

    Ong obtenerPorUsuario(Long usuarioId);

    Ong crearDesdeSolicitud(SolicitudOrganizacion solicitud);

    void suspender(Long ongId);

    void activar(Long ongId);

    List<Ong> findAll();
}