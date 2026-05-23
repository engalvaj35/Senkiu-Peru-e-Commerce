package com.senkiu.admin.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.senkiu.admin.dto.UsuarioAdminDTO;
import com.senkiu.admin.service.AdminService;
import com.senkiu.empresa.service.EmpresaService;
import com.senkiu.ong.service.OngService;
import com.senkiu.solicitud.model.SolicitudOrganizacion;
import com.senkiu.solicitud.service.SolicitudOrganizacionService;
import com.senkiu.solicitud.producto.service.SolicitudProductoService;
import com.senkiu.solicitud.producto.dto.SolicitudProductoResponse;
import com.senkiu.solicitud.programa.model.SolicitudPrograma;
import com.senkiu.solicitud.programa.service.SolicitudProgramaService;
import com.senkiu.rifa.ticket.dto.TicketRifaDetalleResponse;
import com.senkiu.ticket.service.TicketService;

@RestController
@RequestMapping("/admin/api")
@PreAuthorize("hasRole('ADMIN')")
public class AdminApiController {

    private final AdminService adminService;
    private final SolicitudOrganizacionService solicitudService;
    private final EmpresaService empresaService;
    private final OngService ongService;
    private final SolicitudProductoService solicitudProductoService;
    private final SolicitudProgramaService solicitudProgramaService;
    private final TicketService ticketService;

    public AdminApiController(
            AdminService adminService,
            SolicitudOrganizacionService solicitudService,
            EmpresaService empresaService,
            OngService ongService,
            SolicitudProductoService solicitudProductoService,
            SolicitudProgramaService solicitudProgramaService,
            TicketService ticketService
    ) {
        this.adminService = adminService;
        this.solicitudService = solicitudService;
        this.empresaService = empresaService;
        this.ongService = ongService;
        this.solicitudProductoService = solicitudProductoService;
        this.solicitudProgramaService = solicitudProgramaService;
        this.ticketService = ticketService;
    }

    // ---------------- USERS ----------------

    @GetMapping("/usuarios")
    public List<UsuarioAdminDTO> listarUsuarios() {
        return adminService.listarUsuarios();
    }

    @PutMapping("/usuarios/{id}/rol")
    public void cambiarRol(@PathVariable Long id, @RequestParam String rol) {
        adminService.cambiarRol(id, rol);
    }

    @PutMapping("/usuarios/{id}/suspender")
    public void suspenderUsuario(@PathVariable Long id) {
        adminService.suspenderUsuario(id);
    }

    @PutMapping("/usuarios/{id}/activar")
    public void activarUsuario(@PathVariable Long id) {
        adminService.activarUsuario(id);
    }

    // ---------------- SOLICITUDES ----------------

    @GetMapping("/solicitudes")
    public List<SolicitudOrganizacion> listarSolicitudes() {
        return solicitudService.listarTodas();
    }

    @PutMapping("/solicitudes/{id}/aprobar")
    public void aprobar(@PathVariable Long id) {
        solicitudService.aprobarSolicitud(id);
    }

    @PutMapping("/solicitudes/{id}/rechazar")
    public void rechazar(@PathVariable Long id) {
        solicitudService.rechazarSolicitud(id);
    }

    // ---------------- EMPRESAS ----------------

    @PutMapping("/empresas/{id}/suspender")
    public void suspenderEmpresa(@PathVariable Long id) {
        empresaService.suspender(id);
    }

    @PutMapping("/empresas/{id}/activar")
    public void activarEmpresa(@PathVariable Long id) {
        empresaService.activar(id);
    }

    // ---------------- ONGS ----------------

    @PutMapping("/ongs/{id}/suspender")
    public void suspenderOng(@PathVariable Long id) {
        ongService.suspender(id);
    }

    @PutMapping("/ongs/{id}/activar")
    public void activarOng(@PathVariable Long id) {
        ongService.activar(id);
    }

    // ---------------- PRODUCTOS ----------------

    @GetMapping("/productos/solicitudes")
    public List<SolicitudProductoResponse> listarSolicitudesProducto() {
        return solicitudProductoService.listarTodas();
    }

    @PutMapping("/productos/solicitudes/{id}/aprobar")
    public void aprobarProducto(@PathVariable Long id) {
        solicitudProductoService.aprobar(id);
    }

    @PutMapping("/productos/solicitudes/{id}/rechazar")
    public void rechazarProducto(@PathVariable Long id) {
        solicitudProductoService.rechazar(id);
    }

    // ---------------- PROGRAMAS ----------------

    @GetMapping("/programas/solicitudes")
    public List<SolicitudPrograma> listarSolicitudesPrograma() {
        return solicitudProgramaService.listarTodas();
    }

    @PutMapping("/programas/solicitudes/{id}/aprobar")
    public void aprobarPrograma(@PathVariable Long id) {
        solicitudProgramaService.aprobar(id);
    }

    @PutMapping("/programas/solicitudes/{id}/rechazar")
    public void rechazarPrograma(@PathVariable Long id) {
        solicitudProgramaService.rechazar(id);
    }

    // ---------------- TICKETS ----------------

    @GetMapping("/rifas/{rifaId}/tickets")
    public List<TicketRifaDetalleResponse> ticketsPorRifa(@PathVariable Long rifaId) {
        return ticketService.listarTicketsPorRifa(rifaId);
    }
}