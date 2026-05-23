package com.senkiu.solicitud.producto.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.senkiu.storage.service.StorageService;

import com.senkiu.solicitud.producto.dto.SolicitudProductoRequest;
import com.senkiu.solicitud.producto.dto.SolicitudProductoResponse;
import com.senkiu.solicitud.producto.model.SolicitudProducto;
import com.senkiu.solicitud.producto.repository.SolicitudProductoRepository;
import com.senkiu.solicitud.producto.service.SolicitudProductoService;

import com.senkiu.estado.publicacion.model.EstadoPublicacion;
import com.senkiu.estado.publicacion.repository.EstadoPublicacionRepository;

import com.senkiu.producto.model.Producto;
import com.senkiu.producto.repository.ProductoRepository;

import com.senkiu.empresa.repository.EmpresaRepository;
import com.senkiu.categoria.repository.CategoriaRepository;

import java.util.List;

@Service
public class SolicitudProductoServiceImpl implements SolicitudProductoService {

    private final SolicitudProductoRepository repo;
    private final EmpresaRepository empresaRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstadoPublicacionRepository estadoRepository;
    private final ProductoRepository productoRepository;
    private final StorageService storageService;

    public SolicitudProductoServiceImpl(
            SolicitudProductoRepository repo,
            EmpresaRepository empresaRepository,
            CategoriaRepository categoriaRepository,
            EstadoPublicacionRepository estadoRepository,
            ProductoRepository productoRepository,
            StorageService storageService
    ) {
        this.repo = repo;
        this.empresaRepository = empresaRepository;
        this.categoriaRepository = categoriaRepository;
        this.estadoRepository = estadoRepository;
        this.productoRepository = productoRepository;
        this.storageService = storageService;
    }

    @Override
    public void crearSolicitud(Long empresaId, SolicitudProductoRequest req, MultipartFile archivo) {

        EstadoPublicacion pendiente = estadoRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no existe"));

        String url = storageService.guardarArchivo(archivo);

        SolicitudProducto sol = new SolicitudProducto();

        sol.setTipo("PRODUCTO");
        sol.setEmpresaId(empresaId);
        sol.setNombre(req.getNombre());
        sol.setDescripcion(req.getDescripcion());
        sol.setPrecio(req.getPrecio());
        sol.setStock(req.getStock());
        sol.setCategoria(
                categoriaRepository.findById(req.getCategoriaId())
                        .orElseThrow(() -> new RuntimeException("Categoría no existe"))
        );
        sol.setTiempoEntrega(req.getTiempoEntrega());

        // 🖼️ AQUÍ VA LA IMAGEN
        sol.setImagenUrl(url);

        sol.setEstado(pendiente);

        repo.save(sol);
    }

    @Override
        public List<SolicitudProductoResponse> listarTodas() {

        List<SolicitudProducto> lista = repo.findByTipo("PRODUCTO");

        return lista.stream().map(sol -> {

                String nombreEmpresa = empresaRepository.findById(sol.getEmpresaId())
                        .map(e -> e.getNombre())
                        .orElse("Sin empresa");

                return new SolicitudProductoResponse(
                        sol.getId(),
                        sol.getNombre(),
                        sol.getDescripcion(),
                        sol.getPrecio(),
                        sol.getStock(),
                        sol.getImagenUrl(),
                        sol.getEstado().getNombre(),
                        nombreEmpresa
                );
        }).toList();
        }

    @Override
    public void aprobar(Long id) {

        SolicitudProducto sol = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no existe"));

        EstadoPublicacion publicado = estadoRepository.findByNombre("PUBLICADO")
                .orElseThrow(() -> new RuntimeException("Estado PUBLICADO no existe"));

        sol.setEstado(publicado);
        repo.save(sol);

        Producto producto = new Producto();

        producto.setNombre(sol.getNombre());
        producto.setDescripcion(sol.getDescripcion());
        producto.setPrecio(sol.getPrecio());
        producto.setStock(sol.getStock());
        producto.setTiempoEntrega(sol.getTiempoEntrega());
        producto.setImagenUrl(sol.getImagenUrl());

        producto.setEmpresa(
                empresaRepository.findById(sol.getEmpresaId())
                        .orElseThrow(() -> new RuntimeException("Empresa no existe"))
        );

        producto.setCategoria(sol.getCategoria());

        producto.setEstado(publicado);

        productoRepository.save(producto);
    }

    @Override
    public void rechazar(Long id) {

        SolicitudProducto sol = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no existe"));

        EstadoPublicacion rechazado = estadoRepository.findByNombre("RECHAZADO")
                .orElseThrow(() -> new RuntimeException("Estado RECHAZADO no existe"));

        sol.setEstado(rechazado);
        repo.save(sol);
    }
}