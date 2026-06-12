package com.senkiu.carrito.service.impl;

import com.senkiu.carrito.dto.CarritoItemResponse;
import com.senkiu.carrito.dto.CarritoResponse;
import com.senkiu.carrito.model.Carrito;
import com.senkiu.carrito.model.CarritoItem;
import com.senkiu.carrito.repository.CarritoItemRepository;
import com.senkiu.carrito.repository.CarritoRepository;

import com.senkiu.producto.model.Producto;
import com.senkiu.producto.repository.ProductoRepository;
import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;
import com.senkiu.carrito.service.CarritoService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public CarritoServiceImpl(
            CarritoRepository carritoRepository,
            CarritoItemRepository carritoItemRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository) {

        this.carritoRepository = carritoRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Long agregarProducto(Long usuarioId, String sessionId, Long productoId, Integer cantidad) {

        if (cantidad <= 0) {
            throw new RuntimeException("Cantidad inválida");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        Carrito carrito = null;

        if (usuarioId != null) {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            carrito = carritoRepository
                    .findByUsuarioIdAndEmpresaId(usuarioId, producto.getEmpresa().getId())
                    .orElseGet(() -> {
                        Carrito c = new Carrito();
                        c.setUsuario(usuario);
                        c.setEmpresa(producto.getEmpresa());
                        return carritoRepository.save(c);
                    });

        } else {

            carrito = carritoRepository
                    .findBySessionIdAndEmpresaId(sessionId, producto.getEmpresa().getId())
                    .orElseGet(() -> {
                        Carrito c = new Carrito();
                        c.setSessionId(sessionId);
                        c.setEmpresa(producto.getEmpresa());
                        return carritoRepository.save(c);
                    });
        }

        CarritoItem itemExistente =
                carritoItemRepository.findByCarritoIdAndProductoId(carrito.getId(), producto.getId())
                        .orElse(null);

        if (itemExistente != null) {
            int nuevaCantidad = itemExistente.getCantidad() + cantidad;

            if (nuevaCantidad > producto.getStock()) {
                throw new RuntimeException("Stock insuficiente");
            }

            itemExistente.setCantidad(nuevaCantidad);
            carritoItemRepository.save(itemExistente);

            return producto.getEmpresa().getId();
        }

        CarritoItem item = new CarritoItem();
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(cantidad);
        item.setPrecio(producto.getPrecio());

        carritoItemRepository.save(item);

        return producto.getEmpresa().getId();
    }

    @Override
    public CarritoResponse obtenerCarrito(Long usuarioId, String sessionId, Long empresaId) {

        Carrito carrito = (usuarioId != null)
                ? carritoRepository.findByUsuarioIdAndEmpresaId(usuarioId, empresaId)
                        .orElseThrow(() -> new RuntimeException("Carrito no encontrado"))
                : carritoRepository.findBySessionIdAndEmpresaId(sessionId, empresaId)
                        .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        List<CarritoItemResponse> items = carrito.getItems().stream().map(item -> {

            CarritoItemResponse dto = new CarritoItemResponse();
            dto.setItemId(item.getId());
            dto.setProductoId(item.getProducto().getId());
            dto.setProducto(item.getProducto().getNombre());
            dto.setImagenUrl(item.getProducto().getImagenUrl());
            dto.setCantidad(item.getCantidad());
            dto.setPrecio(item.getPrecio());

            BigDecimal subtotal =
                    item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));

            dto.setSubtotal(subtotal);

            return dto;
        }).toList();

        BigDecimal total = items.stream()
                .map(CarritoItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CarritoResponse response = new CarritoResponse();
        response.setCarritoId(carrito.getId());
        response.setEmpresaId(carrito.getEmpresa().getId());
        response.setEmpresa(carrito.getEmpresa().getNombre());
        response.setItems(items);
        response.setTotal(total);

        return response;
    }

    @Override
    public void eliminarItem(Long carritoItemId) {
        carritoItemRepository.deleteById(carritoItemId);
    }

    @Override
    public void vaciarCarrito(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }
}