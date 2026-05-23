package com.senkiu.solicitud.service;

import com.senkiu.solicitud.dto.SolicitudEmpresaRequest;
import com.senkiu.solicitud.dto.SolicitudOngRequest;
import com.senkiu.solicitud.model.SolicitudOrganizacion;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SolicitudOrganizacionService {

    List<SolicitudOrganizacion> listarTodas();

    void crearSolicitudEmpresa(
            Long usuarioId,
            SolicitudEmpresaRequest request,
            MultipartFile archivo
    );

    void crearSolicitudOng(
            Long usuarioId,
            SolicitudOngRequest request,
            MultipartFile archivo
    );

    void aprobarSolicitud(Long id);

    void rechazarSolicitud(Long id);
}