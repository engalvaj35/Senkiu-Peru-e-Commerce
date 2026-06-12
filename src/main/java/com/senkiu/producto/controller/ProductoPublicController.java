package com.senkiu.producto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.senkiu.categoria.repository.CategoriaRepository;
import com.senkiu.producto.service.ProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoPublicController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;

    public ProductoPublicController(ProductoService productoService, CategoriaRepository categoriaRepository) {
        this.productoService = productoService;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String listarProductosPublicos(

            @RequestParam(required = false)
            String nombre,

            @RequestParam(required = false)
            String categoria,

            Model model
    ) {

        model.addAttribute(
                "productos",
                productoService.listarPublicados(
                        nombre,
                        categoria
                )
        );

        model.addAttribute(
                "categorias",
                categoriaRepository.findByTipo(
                        "PRODUCTO"
                )
        );

        model.addAttribute("nombre", nombre);
        model.addAttribute("categoria", categoria);

        return "productos/lista";
    }
}