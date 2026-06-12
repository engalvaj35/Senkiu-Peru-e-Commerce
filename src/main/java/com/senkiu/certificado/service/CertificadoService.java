package com.senkiu.certificado.service;

import org.springframework.web.multipart.MultipartFile;

import com.senkiu.solicitud.model.SolicitudOrganizacion;

public interface CertificadoService {

	void guardarCertificadoSolicitud(SolicitudOrganizacion solicitud, String nombre, MultipartFile archivo);
}