package com.senkiu.producto.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.senkiu.producto.service.ProductoService;
import com.senkiu.empresa.model.Empresa;
import com.senkiu.empresa.service.EmpresaService;
import com.senkiu.security.CustomUserDetails;

@Controller
@RequestMapping("/empresa/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final EmpresaService empresaService;

    public ProductoController(
            ProductoService productoService,
            EmpresaService empresaService
    ) {
        this.productoService = productoService;
        this.empresaService = empresaService;
    }

    @GetMapping
    public String listar(
            @AuthenticationPrincipal CustomUserDetails user,
            Model model
    ) {

        Empresa empresa = empresaService.obtenerPorUsuario(user.getId());

    model.addAttribute("empresa", empresa);

    System.out.println(
        "PRODUCTOS: " +
        productoService.listarPorEmpresa(empresa.getId())
    );

    model.addAttribute(
        "productos",
        productoService.listarPorEmpresa(empresa.getId())
    );

    return "empresa/productos";
    }
}