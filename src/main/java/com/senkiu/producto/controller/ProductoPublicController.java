package com.senkiu.producto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.senkiu.producto.service.ProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoPublicController {

    private final ProductoService productoService;

    public ProductoPublicController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listarProductosPublicos(Model model) {

        model.addAttribute(
                "productos",
                productoService.listarPublicados()
        );

        return "productos/lista";
    }
}