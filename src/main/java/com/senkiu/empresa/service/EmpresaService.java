package com.senkiu.empresa.service;

import com.senkiu.empresa.model.Empresa;
import com.senkiu.solicitud.model.SolicitudOrganizacion;

import java.util.List;

public interface EmpresaService {

    Empresa obtenerPorUsuario(Long usuarioId);

    Empresa crearDesdeSolicitud(SolicitudOrganizacion solicitud);

    void suspender(Long empresaId);

    void activar(Long empresaId);

    List<Empresa> findAll();
}