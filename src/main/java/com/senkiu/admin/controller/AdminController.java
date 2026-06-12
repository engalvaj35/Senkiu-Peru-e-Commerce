package com.senkiu.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.senkiu.admin.service.AdminService;
import com.senkiu.banco.service.CuentaBancariaService;
import com.senkiu.donacion.service.DonacionService;
import com.senkiu.empresa.service.EmpresaService;
import com.senkiu.ong.service.OngService;
import com.senkiu.programa.service.ProgramaSocialService;
import com.senkiu.solicitud.producto.service.SolicitudProductoService;
import com.senkiu.solicitud.programa.service.SolicitudProgramaService;
import com.senkiu.solicitud.service.SolicitudOrganizacionService;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final AdminService adminService;
	private final SolicitudOrganizacionService solicitudService;
	private final EmpresaService empresaService;
	private final OngService ongService;
	private final SolicitudProductoService solicitudProductoService;
	private final SolicitudProgramaService solicitudProgramaService;
	private final CuentaBancariaService cuentaBancariaService;
	private final ProgramaSocialService programaSocialService;
	private final DonacionService donacionService;

	public AdminController(AdminService adminService, SolicitudOrganizacionService solicitudService,
			EmpresaService empresaService, OngService ongService, SolicitudProductoService solicitudProductoService,
			SolicitudProgramaService solicitudProgramaService, CuentaBancariaService cuentaBancariaService,
			ProgramaSocialService programaSocialService, DonacionService donacionService) {
		this.adminService = adminService;
		this.solicitudService = solicitudService;
		this.empresaService = empresaService;
		this.ongService = ongService;
		this.solicitudProductoService = solicitudProductoService;
		this.solicitudProgramaService = solicitudProgramaService;
		this.cuentaBancariaService = cuentaBancariaService;
		this.programaSocialService = programaSocialService;
		this.donacionService = donacionService;
	}

	@GetMapping("/admin/dashboard")
	public String dashboard(Model model) {

		model.addAttribute("usuarios", adminService.listarUsuarios());
		model.addAttribute("solicitudes", solicitudService.listarTodas());

		model.addAttribute("empresas", empresaService.findAll());
		model.addAttribute("ongs", ongService.findAll());

		model.addAttribute("solicitudesProducto", solicitudProductoService.listarTodas());
		model.addAttribute("solicitudesPrograma", solicitudProgramaService.listarTodas());
		model.addAttribute("programasSociales", programaSocialService.listarTodos());

		model.addAttribute("cuentasEmpresa", empresaService.findAll().stream()
				.flatMap(e -> cuentaBancariaService.listarPorEmpresa(e.getId()).stream()).toList());

		model.addAttribute("cuentasOng", ongService.findAll().stream()
				.flatMap(o -> cuentaBancariaService.listarPorOng(o.getId()).stream()).toList());

		model.addAttribute("donaciones", donacionService.listarTodas());

		return "admin/dashboard";
	}

	@GetMapping("/admin/usuarios")
	public String usuarios(Model model) {
		model.addAttribute("usuarios", adminService.listarUsuarios());
		return "admin/usuarios";
	}

	@GetMapping("/admin/organizaciones")
	public String organizaciones(Model model) {
		model.addAttribute("empresas", empresaService.findAll());
		model.addAttribute("ongs", ongService.findAll());
		model.addAttribute("cuentasEmpresa", empresaService.findAll().stream()
				.flatMap(e -> cuentaBancariaService.listarPorEmpresa(e.getId()).stream()).toList());
		model.addAttribute("cuentasOng", ongService.findAll().stream()
				.flatMap(o -> cuentaBancariaService.listarPorOng(o.getId()).stream()).toList());
		return "admin/organizaciones";
	}

	@GetMapping("/admin/solicitudes")
	public String solicitudes(Model model) {
		model.addAttribute("solicitudes", solicitudService.listarTodas());
		model.addAttribute("solicitudesProducto", solicitudProductoService.listarTodas());
		model.addAttribute("solicitudesPrograma", solicitudProgramaService.listarTodas());
		return "admin/solicitudes";
	}

	@GetMapping("/admin/donaciones")
	public String donaciones(Model model) {
		model.addAttribute("donaciones", donacionService.listarTodas());
		return "admin/donaciones";
	}

	@GetMapping("/admin/aprobaciones")
	public String aprobaciones(Model model) {

		model.addAttribute("solicitudesProducto", solicitudProductoService.listarTodas());
		model.addAttribute("solicitudesPrograma", solicitudProgramaService.listarTodas());

		return "admin/aprobaciones";
	}

	@GetMapping("/admin/notificaciones")
	public String notificaciones() {
		return "admin/notificaciones";
	}
}