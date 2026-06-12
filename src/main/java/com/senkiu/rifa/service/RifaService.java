package com.senkiu.rifa.service;

import com.senkiu.rifa.dto.AsociarEmpresaRifaRequest;
import com.senkiu.rifa.dto.CrearRifaRequest;
import com.senkiu.rifa.dto.RifaEmpresaResponse;
import com.senkiu.rifa.dto.RifaResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RifaService {

    void asociarEmpresa(AsociarEmpresaRifaRequest request, Long empresaId);

    List<RifaResponse> listarRifasActivas();

    RifaResponse obtenerDetalle(Long id);

    List<RifaResponse> listarRifasPendientes();

    List<RifaEmpresaResponse> listarEmpresasAsociadas(Long rifaId);

    void activarRifa(Long rifaId);

    List<RifaResponse> listarPorOng(Long ongId);

    RifaResponse crearRifa(CrearRifaRequest request, MultipartFile imagen);

    void enviarRevision(Long rifaId);

    List<RifaResponse> listarRifasPendientesAprobacion();

    List<RifaResponse> listarRifasRechazadas();

    void rechazarRifa(Long rifaId);

    List<RifaResponse> listarPorEmpresa(Long empresaId);

    List<RifaResponse> listarRifasActivas(String titulo);
}