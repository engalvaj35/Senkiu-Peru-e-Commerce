package com.senkiu.certificado.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.senkiu.certificado.model.CertificadoOrganizacion;
import com.senkiu.certificado.repository.CertificadoRepository;
import com.senkiu.certificado.service.CertificadoService;
import com.senkiu.solicitud.model.SolicitudOrganizacion;
import com.senkiu.storage.service.StorageService;

@Service
public class CertificadoServiceImpl
        implements CertificadoService {

    private final CertificadoRepository repo;
    private final StorageService storageService;

    public CertificadoServiceImpl(
            CertificadoRepository repo,
            StorageService storageService
    ) {
        this.repo = repo;
        this.storageService = storageService;
    }

    @Override
    public void guardarCertificadoSolicitud(
            SolicitudOrganizacion solicitud,
            String nombre,
            MultipartFile archivo
    ) {

        String url =
                storageService.guardarArchivo(archivo);

        CertificadoOrganizacion cert =
                new CertificadoOrganizacion();

        cert.setSolicitud(solicitud);
        cert.setNombre(nombre);
        cert.setArchivoUrl(url);

        repo.save(cert);
    }
}