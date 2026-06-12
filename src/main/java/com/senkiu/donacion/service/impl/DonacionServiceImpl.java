package com.senkiu.donacion.service.impl;

import com.senkiu.donacion.dto.DonacionRequest;
import com.senkiu.donacion.model.Donacion;
import com.senkiu.donacion.repository.DonacionRepository;
import com.senkiu.donacion.service.DonacionService;
import com.senkiu.pago.dto.CrearPagoRequest;
import com.senkiu.pago.model.MetodoPago;
import com.senkiu.pago.model.TipoPago;
import com.senkiu.pago.service.PagoService;
import com.senkiu.programa.model.ProgramaSocial;
import com.senkiu.programa.repository.ProgramaSocialRepository;
import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;
import com.senkiu.pago.model.Pago;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DonacionServiceImpl implements DonacionService {

    private final DonacionRepository donacionRepository;
    private final ProgramaSocialRepository programaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PagoService pagoService;

    public DonacionServiceImpl(
            DonacionRepository donacionRepository,
            ProgramaSocialRepository programaRepository,
            UsuarioRepository usuarioRepository,
            PagoService pagoService
    ) {
        this.donacionRepository = donacionRepository;
        this.programaRepository = programaRepository;
        this.usuarioRepository = usuarioRepository;
        this.pagoService = pagoService;
    }

    @Override
    @Transactional
    public Donacion donar(Long usuarioId, DonacionRequest request) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        ProgramaSocial programa = programaRepository.findById(request.getProgramaId())
                .orElseThrow(() -> new RuntimeException("Programa no existe"));

        Donacion donacion = new Donacion();
        donacion.setUsuario(usuario);
        donacion.setPrograma(programa);
        donacion.setMonto(request.getMonto());

        Donacion donacionGuardada = donacionRepository.save(donacion);

        CrearPagoRequest pagoRequest = new CrearPagoRequest();

        pagoRequest.setUsuarioId(usuarioId);
        pagoRequest.setTipo(TipoPago.DONACION);
        pagoRequest.setReferenciaId(donacionGuardada.getId());
        pagoRequest.setMonto(request.getMonto());
        pagoRequest.setMetodo(MetodoPago.MOCK);

        Pago pago = pagoService.crearPago(pagoRequest);

        donacionGuardada.setPago(pago);

        donacionRepository.save(donacionGuardada);

        return donacionGuardada;
    }

    @Override
    public List<Donacion> listarPorUsuario(Long usuarioId) {

        return donacionRepository
                .findByUsuarioIdOrderByCreatedAtDesc(usuarioId);
    }

    @Override
    public List<Donacion> listarTodas() {

        return donacionRepository
                .findAllByOrderByCreatedAtDesc();
    }
        @Override
        public Donacion buscarPorId(Long donacionId) {

        return donacionRepository.findById(donacionId)
                .orElseThrow(() ->
                        new RuntimeException("Donación no encontrada"));
        }
}