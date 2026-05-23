package com.senkiu.producto.service.impl;

import org.springframework.stereotype.Service;

import com.senkiu.producto.service.ProductoService;
import com.senkiu.solicitud.producto.model.SolicitudProducto;
import com.senkiu.producto.repository.ProductoRepository;
import com.senkiu.producto.dto.ProductoResponse;
import com.senkiu.producto.model.Producto;
import com.senkiu.producto.mapper.ProductoMapper;

import com.senkiu.empresa.repository.EmpresaRepository;
import com.senkiu.categoria.repository.CategoriaRepository;
import com.senkiu.estado.publicacion.repository.EstadoPublicacionRepository;
import com.senkiu.estado.publicacion.model.EstadoPublicacion;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final EmpresaRepository empresaRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstadoPublicacionRepository estadoRepository;

    public ProductoServiceImpl(
            ProductoRepository productoRepository,
            EmpresaRepository empresaRepository,
            CategoriaRepository categoriaRepository,
            EstadoPublicacionRepository estadoRepository
    ) {
        this.productoRepository = productoRepository;
        this.empresaRepository = empresaRepository;
        this.categoriaRepository = categoriaRepository;
        this.estadoRepository = estadoRepository;
    }

    // =========================
    // CREAR DESDE SOLICITUD (FLUJO PRINCIPAL)
    // =========================
    @Override
    public Producto crearDesdeSolicitud(SolicitudProducto sol) {

        EstadoPublicacion publicado = estadoRepository.findByNombre("PUBLICADO")
                .orElseThrow(() -> new RuntimeException("Estado PUBLICADO no existe"));

        Producto producto = new Producto();

        producto.setNombre(sol.getNombre());
        producto.setDescripcion(sol.getDescripcion());
        producto.setPrecio(sol.getPrecio());
        producto.setStock(sol.getStock());
        producto.setTiempoEntrega(sol.getTiempoEntrega());

        producto.setEmpresa(
                empresaRepository.findById(sol.getEmpresaId())
                        .orElseThrow(() -> new RuntimeException("Empresa no existe"))
        );

        producto.setCategoria(sol.getCategoria());

        producto.setEstado(publicado);

        return productoRepository.save(producto);
    }

    // =========================
    // LISTAR POR EMPRESA
    // =========================
    @Override
        public List<ProductoResponse> listarPorEmpresa(Long empresaId) {

        return productoRepository.findByEmpresaId(empresaId)
                .stream()
                .map(ProductoMapper::toDto)
                .toList();
        }

        @Override
        public List<ProductoResponse> listarPublicados() {

        return productoRepository
                .findByEstadoNombre("PUBLICADO")
                .stream()
                .map(p -> {

                        ProductoResponse dto = new ProductoResponse();

                        dto.setId(p.getId());
                        dto.setNombre(p.getNombre());
                        dto.setPrecio(p.getPrecio());
                        dto.setStock(p.getStock());
                        dto.setImagenUrl(p.getImagenUrl());

                        dto.setEmpresaId(
                                p.getEmpresa().getId()
                        );

                        dto.setCategoria(
                                p.getCategoria().getNombre()
                        );

                        dto.setEstado(
                                p.getEstado().getNombre()
                        );

                        dto.setEmpresa(
                                p.getEmpresa().getNombre()
                        );

                        return dto;
                })
                .toList();
        }
}