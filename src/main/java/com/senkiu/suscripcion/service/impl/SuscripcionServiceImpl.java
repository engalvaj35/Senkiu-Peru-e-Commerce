package com.senkiu.suscripcion.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senkiu.suscripcion.model.PlanSuscripcion;
import com.senkiu.suscripcion.model.Suscripcion;
import com.senkiu.suscripcion.service.SuscripcionService;
import com.senkiu.suscripcion.repository.SuscripcionRepository;

import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;

import com.senkiu.estado.suscripcion.model.EstadoSuscripcion;
import com.senkiu.estado.suscripcion.repository.EstadoSuscripcionRepository;

import com.senkiu.pago.model.Pago;
import com.senkiu.pago.service.PagoService;

import com.senkiu.pago.dto.CrearPagoRequest;
import com.senkiu.pago.model.TipoPago;
import com.senkiu.pago.model.MetodoPago;

import java.time.LocalDateTime;

@Service
public class SuscripcionServiceImpl implements SuscripcionService {

    private final SuscripcionRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoSuscripcionRepository estadoRepository;
    private final PagoService pagoService;

    public SuscripcionServiceImpl(
            SuscripcionRepository repository,
            UsuarioRepository usuarioRepository,
            EstadoSuscripcionRepository estadoRepository,
            PagoService pagoService
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.estadoRepository = estadoRepository;
        this.pagoService = pagoService;
    }

    @Override
    @Transactional
    public Suscripcion crearSuscripcion(Long usuarioId, String plan) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PlanSuscripcion planEnum;

        try {
            planEnum = PlanSuscripcion.valueOf(plan.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Plan inválido: " + plan);
        }

        repository.findByUsuario(usuario)
                .ifPresent(s -> {
                    throw new RuntimeException("El usuario ya tiene una suscripción");
                });

        EstadoSuscripcion estadoPendiente = estadoRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no existe"));

        // 1. crear suscripción (PENDIENTE)
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setUsuario(usuario);
        suscripcion.setPlan(planEnum.name());
        suscripcion.setPrecio(planEnum.getPrecio());

        LocalDateTime ahora = LocalDateTime.now();
        suscripcion.setFechaInicio(ahora);
        suscripcion.setFechaFin(ahora.plusMonths(1));

        suscripcion.setEstado(estadoPendiente);

        Suscripcion saved = repository.save(suscripcion);

        // 2. crear pago
        CrearPagoRequest pagoRequest = new CrearPagoRequest();
        pagoRequest.setUsuarioId(usuarioId);
        pagoRequest.setTipo(TipoPago.SUSCRIPCION);
        pagoRequest.setReferenciaId(saved.getId());
        pagoRequest.setMonto(planEnum.getPrecio());
        pagoRequest.setMetodo(MetodoPago.MOCK);

        Pago pago = pagoService.crearPago(pagoRequest);

        // 3. enlazar pago
        saved.setPago(pago);

        return repository.save(saved);
    }

    @Override
    public Suscripcion obtenerPorUsuario(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        return repository.findByUsuario(usuario)
                .orElse(null);
    }
}