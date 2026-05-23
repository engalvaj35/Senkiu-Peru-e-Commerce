package com.senkiu.certificado.repository;

import com.senkiu.certificado.model.CertificadoOrganizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificadoRepository
        extends JpaRepository<CertificadoOrganizacion, Long> {

    List<CertificadoOrganizacion>
    findBySolicitudId(Long solicitudId);
}