package com.senkiu.producto.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.senkiu.producto.model.Producto;
import com.senkiu.producto.repository.ProductoRepository;
import com.senkiu.producto.service.ProductoAdminService;

import java.util.List;

@RestController
@RequestMapping("/admin/api/productos")
@PreAuthorize("hasRole('ADMIN')")
public class ProductoAdminController {

    private final ProductoAdminService productoAdminService;
    private final ProductoRepository productoRepository;

    public ProductoAdminController(
            ProductoAdminService productoAdminService,
            ProductoRepository productoRepository
    ) {
        this.productoAdminService = productoAdminService;
        this.productoRepository = productoRepository;
    }

    // =========================
    // LISTAR PRODUCTOS PENDIENTES
    // =========================
    @GetMapping("/pendientes")
    public List<Producto> listarPendientes() {
        return productoRepository.findByEstadoNombre("PENDIENTE");
    }

    // =========================
    // APROBAR PRODUCTO
    // =========================
    @PutMapping("/{id}/aprobar")
    public void aprobar(@PathVariable Long id) {
        productoAdminService.aprobarProducto(id);
    }

    // =========================
    // RECHAZAR PRODUCTO
    // =========================
    @PutMapping("/{id}/rechazar")
    public void rechazar(@PathVariable Long id) {
        productoAdminService.rechazarProducto(id);
    }

    // =========================
    // LISTAR TODOS (OPCIONAL)
    // =========================
    @GetMapping
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }
}