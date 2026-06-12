package com.senkiu.usuario.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.senkiu.donacion.service.DonacionService;
import com.senkiu.orden.service.OrdenService;
import com.senkiu.security.CustomUserDetails;
import com.senkiu.suscripcion.service.SuscripcionService;
import com.senkiu.ticket.service.TicketService;
import com.senkiu.usuario.dto.CambiarPasswordRequest;
import com.senkiu.usuario.dto.UsuarioPerfilRequest;
import com.senkiu.usuario.service.UsuarioService;
import com.senkiu.wallet.service.WalletService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/perfil")
public class UsuarioViewController {

	private final UsuarioService service;
	private final OrdenService ordenService;
	private final SuscripcionService suscripcionService;
	private final DonacionService donacionService;
	private final TicketService ticketService;
	private final WalletService walletService;

	public UsuarioViewController(UsuarioService service, OrdenService ordenService,
			SuscripcionService suscripcionService, DonacionService donacionService, TicketService ticketService,
			WalletService walletService) {
		this.service = service;
		this.ordenService = ordenService;
		this.suscripcionService = suscripcionService;
		this.donacionService = donacionService;
		this.ticketService = ticketService;
		this.walletService = walletService;
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public String verPerfil(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

		model.addAttribute("perfil", service.obtenerPerfil(userDetails.getId()));

		model.addAttribute("ordenes", ordenService.listarPorUsuario(userDetails.getId()));

		model.addAttribute("passwordRequest", new CambiarPasswordRequest());

		model.addAttribute("suscripcion", suscripcionService.obtenerPorUsuario(userDetails.getId()));

		model.addAttribute("donaciones", donacionService.listarPorUsuario(userDetails.getId()));

		model.addAttribute("tickets", ticketService.obtenerMisTickets(userDetails.getId()));

		model.addAttribute("saldo", walletService.obtenerSaldo(userDetails.getId()));

		return "profile";
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public String actualizarPerfil(@Valid @ModelAttribute("perfil") UsuarioPerfilRequest request, BindingResult result,
			@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest requestHttp,
			HttpServletResponse responseHttp, RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {

			model.addAttribute("ordenes", ordenService.listarPorUsuario(userDetails.getId()));

			model.addAttribute("passwordRequest", new CambiarPasswordRequest());

			model.addAttribute("suscripcion", suscripcionService.obtenerPorUsuario(userDetails.getId()));

			model.addAttribute("donaciones", donacionService.listarPorUsuario(userDetails.getId()));

			model.addAttribute("tickets", ticketService.obtenerMisTickets(userDetails.getId()));

			model.addAttribute("saldo", walletService.obtenerSaldo(userDetails.getId()));

			return "profile";
		}

		try {

			String emailActual = userDetails.getEmail();

			service.completarPerfil(userDetails.getId(), request);

			if (!emailActual.equals(request.getEmail())) {

				new SecurityContextLogoutHandler().logout(requestHttp, responseHttp, null);

				return "redirect:/login-page?emailChanged";
			}

			redirectAttributes.addFlashAttribute("success", "Perfil actualizado correctamente");

		} catch (RuntimeException e) {

			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}

		return "redirect:/perfil";
	}

	@PostMapping("/password")
	@PreAuthorize("isAuthenticated()")
	public String cambiarPassword(@Valid @ModelAttribute("passwordRequest") CambiarPasswordRequest request,

			BindingResult result,

			@AuthenticationPrincipal CustomUserDetails userDetails,

			HttpServletRequest requestHttp, HttpServletResponse responseHttp,

			RedirectAttributes redirectAttributes, Model model) {

		if (result.hasErrors()) {

			model.addAttribute("perfil", service.obtenerPerfil(userDetails.getId()));

			model.addAttribute("ordenes", ordenService.listarPorUsuario(userDetails.getId()));

			model.addAttribute("suscripcion", suscripcionService.obtenerPorUsuario(userDetails.getId()));

			model.addAttribute("donaciones", donacionService.listarPorUsuario(userDetails.getId()));

			model.addAttribute("tickets", ticketService.obtenerMisTickets(userDetails.getId()));

			model.addAttribute("saldo", walletService.obtenerSaldo(userDetails.getId()));

			return "profile";
		}

		try {

			service.cambiarPassword(userDetails.getId(), request);

			new SecurityContextLogoutHandler().logout(requestHttp, responseHttp, null);

			return "redirect:/login-page?passwordChanged";

		} catch (RuntimeException e) {

			redirectAttributes.addFlashAttribute("passwordError", e.getMessage());

			return "redirect:/perfil";
		}
	}
}