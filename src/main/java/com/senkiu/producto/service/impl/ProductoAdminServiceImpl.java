package com.senkiu.producto.service.impl;

import org.springframework.stereotype.Service;

import com.senkiu.producto.model.Producto;
import com.senkiu.producto.repository.ProductoRepository;
import com.senkiu.producto.service.ProductoAdminService;

import com.senkiu.estado.publicacion.model.EstadoPublicacion;
import com.senkiu.estado.publicacion.repository.EstadoPublicacionRepository;

@Service
public class ProductoAdminServiceImpl implements ProductoAdminService {

    private final ProductoRepository productoRepository;
    private final EstadoPublicacionRepository estadoRepository;

    public ProductoAdminServiceImpl(
            ProductoRepository productoRepository,
            EstadoPublicacionRepository estadoRepository
    ) {
        this.productoRepository = productoRepository;
        this.estadoRepository = estadoRepository;
    }

    @Override
    public void aprobarProducto(Long id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no existe"));

        EstadoPublicacion estado = estadoRepository.findByNombre("PUBLICADO")
                .orElseThrow(() -> new RuntimeException("Estado PUBLICADO no existe"));

        producto.setEstado(estado);
        productoRepository.save(producto);
    }

    @Override
    public void rechazarProducto(Long id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no existe"));

        EstadoPublicacion estado = estadoRepository.findByNombre("RECHAZADO")
                .orElseThrow(() -> new RuntimeException("Estado RECHAZADO no existe"));

        producto.setEstado(estado);
        productoRepository.save(producto);
    }
}