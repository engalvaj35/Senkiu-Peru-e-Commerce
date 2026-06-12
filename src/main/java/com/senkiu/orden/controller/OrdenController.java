package com.senkiu.orden.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.senkiu.orden.dto.OrdenRequest;
import com.senkiu.orden.model.Orden;
import com.senkiu.orden.repository.OrdenRepository;
import com.senkiu.orden.service.OrdenService;
import com.senkiu.pago.model.MetodoPago;
import com.senkiu.security.CustomUserDetails;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/orden")
public class OrdenController {

	private final OrdenService ordenService;
	private final OrdenRepository ordenRepository;

	public OrdenController(OrdenService ordenService, OrdenRepository ordenRepository) {
		this.ordenService = ordenService;
		this.ordenRepository = ordenRepository;
	}

	@PostMapping("/crear/{empresaId}")
	public String crearOrden(
			@PathVariable Long empresaId,
			@RequestParam MetodoPago metodoPago,
			@AuthenticationPrincipal CustomUserDetails user,
			HttpSession session,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String telefono,
			@RequestParam(required = false) String direccion,
			RedirectAttributes redirectAttributes) {

		try {

			Long usuarioId = (user != null) ? user.getId() : null;
			String sessionId = session.getId();

			OrdenRequest request = new OrdenRequest();
			request.setUsuarioId(usuarioId);
			request.setSessionId(sessionId); 
			request.setEmpresaId(empresaId);
			request.setMetodoPago(metodoPago);
			request.setEmail(email);
			request.setTelefono(telefono);
			request.setDireccion(direccion);

			Orden orden = ordenService.crearOrden(request);

			return "redirect:/orden/" + orden.getId() + "/pago";

		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/carrito/" + empresaId;
		}
	}

	@GetMapping("/{ordenId}/pago")
	public String vistaPagoOrden(@PathVariable Long ordenId, Model model) {
		Orden orden = ordenRepository.findById(ordenId).orElseThrow(() -> new RuntimeException("Orden no encontrada"));

		model.addAttribute("orden", orden);

		return "orden/orden-pago";
	}

	@PostMapping("/{ordenId}/estado")
	public String cambiarEstado(@PathVariable Long ordenId, @RequestParam String estado) {
		ordenService.cambiarEstado(ordenId, estado);

		return "redirect:/empresa/ordenes";
	}
}