package com.senkiu.banco.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.senkiu.banco.dto.CuentaBancariaRequest;
import com.senkiu.banco.dto.CuentaBancariaResponse;
import com.senkiu.banco.model.CuentaBancaria;
import com.senkiu.banco.repository.CuentaBancariaRepository;
import com.senkiu.banco.service.CuentaBancariaService;
import com.senkiu.empresa.model.Empresa;
import com.senkiu.empresa.repository.EmpresaRepository;
import com.senkiu.ong.model.Ong;
import com.senkiu.solicitud.model.SolicitudOrganizacion;
import com.senkiu.ong.repository.OngRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CuentaBancariaServiceImpl
        implements CuentaBancariaService {

    private final CuentaBancariaRepository repository;
    private final EmpresaRepository empresaRepository;
    private final OngRepository ongRepository;
    private final ObjectMapper objectMapper;

    public CuentaBancariaServiceImpl(
            CuentaBancariaRepository repository,
            EmpresaRepository empresaRepository,
            OngRepository ongRepository,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.ongRepository = ongRepository;
        this.objectMapper = objectMapper;
    }

    private void validarDuplicados(
                CuentaBancariaRequest request
        ) {

        if (
                repository.existsByNumeroCuenta(
                        request.getNumeroCuenta()
                )
        ) {

                throw new RuntimeException(
                        "La cuenta bancaria ya existe"
                );
        }

        if (
                request.getCci() != null &&
                !request.getCci().isBlank() &&
                repository.existsByCci(
                        request.getCci()
                )
        ) {

                throw new RuntimeException(
                        "El CCI ya existe"
                );
        }
        }

    @Override
    public CuentaBancariaResponse crearParaEmpresa(
            Long empresaId,
            CuentaBancariaRequest request
    ) {

        validarDuplicados(request);

        Empresa empresa =
                empresaRepository.findById(empresaId)
                        .orElseThrow();

        CuentaBancaria cuenta =
                new CuentaBancaria();

        cuenta.setTitular(request.getTitular());
        cuenta.setBanco(request.getBanco());
        cuenta.setTipoCuenta(request.getTipoCuenta());
        cuenta.setNumeroCuenta(request.getNumeroCuenta());
        cuenta.setCci(request.getCci());
        cuenta.setMoneda(request.getMoneda());
        cuenta.setEmpresa(empresa);

        repository.save(cuenta);

        return mapToResponse(cuenta);
    }

    @Override
    public CuentaBancariaResponse crearParaOng(
            Long ongId,
            CuentaBancariaRequest request
    ) {

        validarDuplicados(request);

        Ong ong =
                ongRepository.findById(ongId)
                        .orElseThrow();

        CuentaBancaria cuenta =
                new CuentaBancaria();

        cuenta.setTitular(request.getTitular());
        cuenta.setBanco(request.getBanco());
        cuenta.setTipoCuenta(request.getTipoCuenta());
        cuenta.setNumeroCuenta(request.getNumeroCuenta());
        cuenta.setCci(request.getCci());
        cuenta.setMoneda(request.getMoneda());
        cuenta.setOng(ong);

        repository.save(cuenta);

        return mapToResponse(cuenta);
    }

    @Override
    public List<CuentaBancariaResponse> listarPorEmpresa(
            Long empresaId
    ) {

        return repository.findByEmpresaId(empresaId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CuentaBancariaResponse> listarPorOng(
            Long ongId
    ) {

        return repository.findByOngId(ongId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

        private CuentaBancariaResponse mapToResponse(CuentaBancaria cuenta) {

        CuentaBancariaResponse response = new CuentaBancariaResponse();

        response.setId(cuenta.getId());
        response.setTitular(cuenta.getTitular());
        response.setBanco(cuenta.getBanco());
        response.setTipoCuenta(cuenta.getTipoCuenta());
        response.setNumeroCuenta(cuenta.getNumeroCuenta());
        response.setCci(cuenta.getCci());
        response.setMoneda(cuenta.getMoneda());

        // 🔥 NUEVO: relaciones
        response.setEmpresaId(
                cuenta.getEmpresa() != null ? cuenta.getEmpresa().getId() : null
        );

        response.setOngId(
                cuenta.getOng() != null ? cuenta.getOng().getId() : null
        );

        return response;
        }

    @Override
    public void crearDesdeSolicitudEmpresa(
            Long empresaId,
            SolicitudOrganizacion solicitud
    ) {

        try {

            JsonNode json =
                    objectMapper.readTree(
                            solicitud.getDatosJson()
                    );

            CuentaBancariaRequest request =
                    new CuentaBancariaRequest();

            request.setTitular(
                    json.get("titularCuenta").asText()
            );

            request.setBanco(
                    json.get("banco").asText()
            );

            request.setTipoCuenta(
                    json.get("tipoCuenta").asText()
            );

            request.setNumeroCuenta(
                    json.get("numeroCuenta").asText()
            );

            request.setCci(
                    json.get("cci").asText()
            );

            request.setMoneda(
                    json.get("moneda").asText()
            );

            crearParaEmpresa(empresaId, request);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void crearDesdeSolicitudOng(
            Long ongId,
            SolicitudOrganizacion solicitud
    ) {

        try {

            JsonNode json =
                    objectMapper.readTree(
                            solicitud.getDatosJson()
                    );

            CuentaBancariaRequest request =
                    new CuentaBancariaRequest();

            request.setTitular(
                    json.get("titularCuenta").asText()
            );

            request.setBanco(
                    json.get("banco").asText()
            );

            request.setTipoCuenta(
                    json.get("tipoCuenta").asText()
            );

            request.setNumeroCuenta(
                    json.get("numeroCuenta").asText()
            );

            request.setCci(
                    json.get("cci").asText()
            );

            request.setMoneda(
                    json.get("moneda").asText()
            );

            crearParaOng(ongId, request);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}