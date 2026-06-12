package com.senkiu.entrega.controller;

import org.springframework.web.bind.annotation.*;

import com.senkiu.entrega.dto.ActualizarEntregaRequest;
import com.senkiu.entrega.dto.EntregaResponse;
import com.senkiu.entrega.service.EntregaService;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

	private final EntregaService entregaService;

	public EntregaController(EntregaService entregaService) {
		this.entregaService = entregaService;
	}

	@GetMapping("/orden/{ordenId}")
	public EntregaResponse obtenerEntrega(@PathVariable Long ordenId) {
		return entregaService.obtenerPorOrden(ordenId);
	}

	@PutMapping("/orden/{ordenId}")
	public EntregaResponse actualizarEntrega(@PathVariable Long ordenId,
			@RequestBody ActualizarEntregaRequest request) {
		return entregaService.actualizarEstado(ordenId, request);
	}
}