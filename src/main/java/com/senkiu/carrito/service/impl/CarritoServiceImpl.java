package com.senkiu.carrito.service.impl;

import com.senkiu.carrito.dto.CarritoItemResponse;
import com.senkiu.carrito.dto.CarritoResponse;
import com.senkiu.carrito.model.Carrito;
import com.senkiu.carrito.model.CarritoItem;
import com.senkiu.carrito.repository.CarritoItemRepository;
import com.senkiu.carrito.repository.CarritoRepository;
import com.senkiu.carrito.service.CarritoService;

import com.senkiu.producto.model.Producto;
import com.senkiu.producto.repository.ProductoRepository;

import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            UsuarioRepository usuarioRepository
    ) {
        this.carritoRepository = carritoRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // =========================
    // AGREGAR PRODUCTO
    // =========================

    @Override
    public Long agregarProducto(
            Long usuarioId,
            Long productoId,
            Integer cantidad
    ) {

        if (cantidad <= 0) {
            throw new RuntimeException("Cantidad inválida");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado")
                );

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado")
                );

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEmpresaId(
                        usuario.getId(),
                        producto.getEmpresa().getId()
                )
                .orElseGet(() -> {

                    Carrito nuevo = new Carrito();

                    nuevo.setUsuario(usuario);
                    nuevo.setEmpresa(producto.getEmpresa());

                    return carritoRepository.save(nuevo);
                });

        CarritoItem itemExistente = carritoItemRepository
                .findByCarritoIdAndProductoId(
                        carrito.getId(),
                        producto.getId()
                )
                .orElse(null);

        // SI YA EXISTE
        if (itemExistente != null) {

            int nuevaCantidad =
                    itemExistente.getCantidad() + cantidad;

            if (nuevaCantidad > producto.getStock()) {
                throw new RuntimeException("Stock insuficiente");
            }

            itemExistente.setCantidad(nuevaCantidad);

            carritoItemRepository.save(itemExistente);

            return producto.getEmpresa().getId();
        }

        // NUEVO ITEM
        CarritoItem item = new CarritoItem();

        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(cantidad);

        // precio congelado
        item.setPrecio(producto.getPrecio());

        carritoItemRepository.save(item);

        return producto.getEmpresa().getId();
    }

    // =========================
    // OBTENER CARRITO
    // =========================

    @Override
    public CarritoResponse obtenerCarrito(
            Long usuarioId,
            Long empresaId
    ) {

        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEmpresaId(
                        usuarioId,
                        empresaId
                )
                .orElseThrow(() ->
                        new RuntimeException("Carrito no encontrado")
                );

        List<CarritoItemResponse> items =
                carrito.getItems()
                        .stream()
                        .map(item -> {

                            CarritoItemResponse dto =
                                    new CarritoItemResponse();

                            dto.setItemId(item.getId());

                            dto.setProductoId(
                                    item.getProducto().getId()
                            );

                            dto.setProducto(
                                    item.getProducto().getNombre()
                            );

                            dto.setImagenUrl(
                                    item.getProducto().getImagenUrl()
                            );

                            dto.setCantidad(
                                    item.getCantidad()
                            );

                            dto.setPrecio(
                                    item.getPrecio()
                            );

                            BigDecimal subtotal =
                                    item.getPrecio().multiply(
                                            BigDecimal.valueOf(
                                                    item.getCantidad()
                                            )
                                    );

                            dto.setSubtotal(subtotal);

                            return dto;
                        })
                        .toList();

        BigDecimal total = items.stream()
                .map(CarritoItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CarritoResponse response =
                new CarritoResponse();

        response.setCarritoId(carrito.getId());

        response.setEmpresaId(
                carrito.getEmpresa().getId()
        );

        response.setEmpresa(
                carrito.getEmpresa().getNombre()
        );

        response.setItems(items);

        response.setTotal(total);

        return response;
    }

    // =========================
    // ELIMINAR ITEM
    // =========================

    @Override
    public void eliminarItem(Long carritoItemId) {

        CarritoItem item = carritoItemRepository
                .findById(carritoItemId)
                .orElseThrow(() ->
                        new RuntimeException("Item no encontrado")
                );

        carritoItemRepository.delete(item);
    }

    // =========================
    // VACIAR CARRITO
    // =========================

    @Override
    public void vaciarCarrito(Long carritoId) {

        Carrito carrito = carritoRepository
                .findById(carritoId)
                .orElseThrow(() ->
                        new RuntimeException("Carrito no encontrado")
                );

        carrito.getItems().clear();

        carritoRepository.save(carrito);
    }
}