package com.senkiu.banco.service;

import java.util.List;

import com.senkiu.banco.dto.CuentaBancariaRequest;
import com.senkiu.banco.dto.CuentaBancariaResponse;
import com.senkiu.solicitud.model.SolicitudOrganizacion;

public interface CuentaBancariaService {

	CuentaBancariaResponse crearParaEmpresa(Long empresaId, CuentaBancariaRequest request);

	CuentaBancariaResponse crearParaOng(Long ongId, CuentaBancariaRequest request);

	List<CuentaBancariaResponse> listarPorEmpresa(Long empresaId);

	List<CuentaBancariaResponse> listarPorOng(Long ongId);

	void crearDesdeSolicitudEmpresa(Long empresaId, SolicitudOrganizacion solicitud);

	void crearDesdeSolicitudOng(Long ongId, SolicitudOrganizacion solicitud);
}