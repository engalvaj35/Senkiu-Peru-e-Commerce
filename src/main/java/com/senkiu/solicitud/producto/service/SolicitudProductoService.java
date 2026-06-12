package com.senkiu.solicitud.producto.service;

import com.senkiu.solicitud.producto.dto.SolicitudProductoRequest;
import com.senkiu.solicitud.producto.dto.SolicitudProductoResponse;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface SolicitudProductoService {

    void crearSolicitud(Long empresaId, SolicitudProductoRequest request,  MultipartFile archivo);

    List<SolicitudProductoResponse> listarTodas();

    void aprobar(Long id);

    void rechazar(Long id);
}