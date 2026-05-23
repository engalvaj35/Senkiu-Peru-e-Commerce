package com.senkiu.empresa.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import com.senkiu.empresa.model.Empresa;
import com.senkiu.empresa.service.EmpresaService;
import com.senkiu.producto.service.ProductoService;
import com.senkiu.security.CustomUserDetails;
import com.senkiu.solicitud.dto.SolicitudEmpresaRequest;
import com.senkiu.solicitud.service.SolicitudOrganizacionService;
import com.senkiu.categoria.repository.CategoriaRepository;
import com.senkiu.orden.service.OrdenService;
import com.senkiu.entrega.service.EntregaService;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    private final EmpresaService service;
    private final SolicitudOrganizacionService solicitudService;
    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;
    private final OrdenService ordenService;
    private final EntregaService entregaService;

    public EmpresaController(
                EmpresaService service,
                SolicitudOrganizacionService solicitudService,
                ProductoService productoService,
                CategoriaRepository categoriaRepository,
                OrdenService ordenService,
                EntregaService entregaService
        ) {
        this.service = service;
        this.solicitudService = solicitudService;
        this.productoService = productoService;
        this.categoriaRepository = categoriaRepository;
        this.ordenService = ordenService;
        this.entregaService = entregaService;
        }

    @GetMapping("/dashboard")
        public String dashboard(
                @AuthenticationPrincipal CustomUserDetails userDetails,
                Model model
        ) {

        Empresa empresa =
                service.obtenerPorUsuario(userDetails.getId());

        if (!empresa.getEstado()
                .getNombre()
                .equals("APROBADO")) {

                return "redirect:/";
        }

        model.addAttribute("empresa", empresa);

        return "empresa/dashboard";
        }

        @GetMapping("/productos-v2")
        public String productos(
                @AuthenticationPrincipal CustomUserDetails userDetails,
                Model model
        ) {

        Empresa empresa =
                service.obtenerPorUsuario(userDetails.getId());

        model.addAttribute("empresa", empresa);

        model.addAttribute(
                "productos",
                productoService.listarPorEmpresa(empresa.getId())
        );

        model.addAttribute(
                "categorias",
                categoriaRepository.findByTipo("PRODUCTO")
        );

        return "empresa/productos";
        }

        @GetMapping("/ordenes")
        public String ordenes(
                @AuthenticationPrincipal CustomUserDetails userDetails,
                Model model
        ) {

        Empresa empresa =
                service.obtenerPorUsuario(userDetails.getId());

        model.addAttribute("empresa", empresa);

        model.addAttribute(
                "ordenes",
                ordenService.listarPorEmpresa(empresa.getId())
        );

        model.addAttribute(
                "entregaService",
                entregaService
        );

        return "empresa/ordenes";
        }

    @GetMapping("/solicitud")
    public String solicitudEmpresa(Model model) {

        model.addAttribute(
                "solicitud",
                new SolicitudEmpresaRequest()
        );

        return "empresa/solicitud";
    }

        @PostMapping("/solicitud")
        public String crearSolicitud(

                @Valid
                @ModelAttribute("solicitud")
                SolicitudEmpresaRequest request,

                BindingResult result,

                @RequestParam("archivo")
                MultipartFile archivo,

                @AuthenticationPrincipal
                CustomUserDetails user,

                Model model
        ) {

        if (result.hasErrors()) {
                return "empresa/solicitud";
        }

        try {

                solicitudService.crearSolicitudEmpresa(
                        user.getId(),
                        request,
                        archivo
                );

                return "redirect:/";

        } catch (RuntimeException e) {

        String mensaje = e.getMessage();

        if (mensaje.contains("número de cuenta")) {

                model.addAttribute(
                        "errorNumeroCuenta",
                        mensaje
                );
        }

        if (mensaje.contains("CCI")) {

                model.addAttribute(
                        "errorCci",
                        mensaje
                );
        }

        return "empresa/solicitud";
        }

        }

        @GetMapping("/rifas/disponibles")
        public String rifasDisponibles(
                @AuthenticationPrincipal CustomUserDetails userDetails,
                Model model
        ) {

        Empresa empresa =
                service.obtenerPorUsuario(userDetails.getId());

        model.addAttribute("empresa", empresa);

        model.addAttribute(
                "productos",
                productoService.listarPorEmpresa(empresa.getId())
        );

        return "empresa/rifas/disponibles";
        }

        @GetMapping("/rifas/mis-rifas")
        public String misRifas(
                @AuthenticationPrincipal CustomUserDetails userDetails,
                Model model
        ) {

        Empresa empresa =
                service.obtenerPorUsuario(userDetails.getId());

        model.addAttribute("empresa", empresa);

        return "empresa/rifas/mis-rifas";
        }
}