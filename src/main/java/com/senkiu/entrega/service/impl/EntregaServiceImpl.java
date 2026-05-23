package com.senkiu.entrega.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.senkiu.entrega.dto.ActualizarEntregaRequest;
import com.senkiu.entrega.dto.EntregaResponse;
import com.senkiu.entrega.model.Entrega;
import com.senkiu.entrega.repository.EntregaRepository;
import com.senkiu.entrega.service.EntregaService;
import com.senkiu.estado.entrega.model.EstadoEntrega;
import com.senkiu.estado.entrega.repository.EstadoEntregaRepository;
import com.senkiu.orden.model.Orden;
import com.senkiu.orden.repository.OrdenRepository;

@Service
public class EntregaServiceImpl
        implements EntregaService {

    private final EntregaRepository entregaRepository;
    private final OrdenRepository ordenRepository;
    private final EstadoEntregaRepository estadoRepository;

    public EntregaServiceImpl(
            EntregaRepository entregaRepository,
            OrdenRepository ordenRepository,
            EstadoEntregaRepository estadoRepository
    ) {
        this.entregaRepository = entregaRepository;
        this.ordenRepository = ordenRepository;
        this.estadoRepository = estadoRepository;
    }

    @Override
    public void crearEntregaInicial(Long ordenId) {

        Orden orden =
                ordenRepository.findById(ordenId)
                        .orElseThrow();

        EstadoEntrega estado =
                estadoRepository
                        .findByNombre("PENDIENTE")
                        .orElseThrow();
        
        String direccion = orden.getUsuario().getDireccion();

        if (direccion == null || direccion.isBlank()) {

                throw new RuntimeException(
                        "Debe completar su dirección antes de realizar una compra"
                );
        }

        Entrega entrega = new Entrega();

        entrega.setOrden(orden);

        entrega.setEstado(estado);

        entrega.setDireccion(direccion);

        entregaRepository.save(entrega);
    }

    @Override
    public EntregaResponse obtenerPorOrden(
            Long ordenId
    ) {

        Entrega entrega =
                entregaRepository.findByOrdenId(ordenId)
                        .orElseThrow();

        return mapToResponse(entrega);
    }

    @Override
    public EntregaResponse actualizarEstado(
            Long ordenId,
            ActualizarEntregaRequest request
    ) {

        Entrega entrega =
                entregaRepository.findByOrdenId(ordenId)
                        .orElseThrow();

        EstadoEntrega estado =
                estadoRepository
                        .findByNombre(request.getEstado())
                        .orElseThrow();

        entrega.setEstado(estado);

        entrega.setComentario(
                request.getComentario()
        );

        if (estado.getNombre().equals("EN_CAMINO")) {

            entrega.setFechaEnvio(
                    LocalDateTime.now()
            );
        }

        if (estado.getNombre().equals("ENTREGADO")) {

            entrega.setFechaEntrega(
                    LocalDateTime.now()
            );
        }

        entregaRepository.save(entrega);

        return mapToResponse(entrega);
    }

    private EntregaResponse mapToResponse(
            Entrega entrega
    ) {

        EntregaResponse response =
                new EntregaResponse();

        response.setId(entrega.getId());

        response.setOrdenId(
                entrega.getOrden().getId()
        );

        response.setEstado(
                entrega.getEstado().getNombre()
        );

        response.setDireccion(
                entrega.getDireccion()
        );

        response.setFechaEnvio(
                entrega.getFechaEnvio()
        );

        response.setFechaEntrega(
                entrega.getFechaEntrega()
        );

        response.setComentario(
                entrega.getComentario()
        );

        return response;
    }
}