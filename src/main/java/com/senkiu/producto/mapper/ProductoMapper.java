package com.senkiu.producto.mapper;

import com.senkiu.producto.dto.ProductoResponse;
import com.senkiu.producto.model.Producto;

public class ProductoMapper {

    public static ProductoResponse toDto(Producto producto) {

        ProductoResponse dto = new ProductoResponse();

        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());

        dto.setCategoria(producto.getCategoria().getNombre());
        dto.setEstado(producto.getEstado().getNombre());
        dto.setEmpresa(producto.getEmpresa().getNombre());
        dto.setImagenUrl(producto.getImagenUrl());

        return dto;
    }
}