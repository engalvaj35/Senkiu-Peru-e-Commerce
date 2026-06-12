package com.senkiu.rifa.service.impl;

import com.senkiu.estado.rifa.model.EstadoRifa;
import com.senkiu.estado.rifa.repository.EstadoRifaRepository;
import com.senkiu.programa.model.ProgramaSocial;
import com.senkiu.programa.repository.ProgramaSocialRepository;
import com.senkiu.rifa.dto.AsociarEmpresaRifaRequest;
import com.senkiu.rifa.dto.CrearRifaRequest;
import com.senkiu.rifa.dto.RifaResponse;
import com.senkiu.rifa.model.Rifa;
import com.senkiu.rifa.model.RifaEmpresa;
import com.senkiu.rifa.dto.RifaEmpresaResponse;
import com.senkiu.rifa.repository.RifaEmpresaRepository;
import com.senkiu.rifa.repository.RifaRepository;
import com.senkiu.rifa.service.RifaService;

import com.senkiu.empresa.model.Empresa;
import com.senkiu.empresa.repository.EmpresaRepository;

import com.senkiu.producto.model.Producto;
import com.senkiu.producto.repository.ProductoRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.senkiu.storage.service.StorageService;

import java.util.List;

@Service
public class RifaServiceImpl implements RifaService {

    private final RifaRepository rifaRepository;
    private final ProgramaSocialRepository programaRepository;
    private final EstadoRifaRepository estadoRifaRepository;
    private final EmpresaRepository empresaRepository;
    private final ProductoRepository productoRepository;
    private final RifaEmpresaRepository rifaEmpresaRepository;
    private final StorageService storageService;

    public RifaServiceImpl(
            RifaRepository rifaRepository,
            ProgramaSocialRepository programaRepository,
            EstadoRifaRepository estadoRifaRepository,
            EmpresaRepository empresaRepository,
            ProductoRepository productoRepository,
            RifaEmpresaRepository rifaEmpresaRepository,
            StorageService storageService
    ) {
        this.rifaRepository = rifaRepository;
        this.programaRepository = programaRepository;
        this.estadoRifaRepository = estadoRifaRepository;
        this.empresaRepository = empresaRepository;
        this.productoRepository = productoRepository;
        this.rifaEmpresaRepository = rifaEmpresaRepository;
        this.storageService = storageService;
    }

    @Override
        public RifaResponse crearRifa(
                CrearRifaRequest request,
                MultipartFile imagen
        ) {

        ProgramaSocial programa =
                programaRepository.findById(
                        request.getProgramaId()
                ).orElseThrow();

        EstadoRifa estado =
                estadoRifaRepository
                        .findByNombre("PENDIENTE")
                        .orElseThrow();

        String imagenUrl = null;

        if (imagen != null && !imagen.isEmpty()) {

                imagenUrl =
                        storageService.guardarArchivo(imagen);

                System.out.println(
                        "Imagen guardada: " + imagenUrl
                );
        }

        Rifa rifa = new Rifa();

        rifa.setPrograma(programa);

        rifa.setTitulo(
                request.getTitulo()
        );

        rifa.setDescripcion(
                request.getDescripcion()
        );

        rifa.setImagenUrl(imagenUrl);

        rifa.setPrecioTicket(
                request.getPrecioTicket()
        );

        rifa.setStockTickets(
                request.getStockTickets()
        );

        rifa.setTicketsVendidos(0);

        rifa.setFechaFin(
                request.getFechaFin()
        );

        rifa.setEstado(estado);

        rifaRepository.save(rifa);

        System.out.println(
                "Imagen en BD: " + rifa.getImagenUrl()
        );

        return mapToResponse(rifa);
        }

    @Override
        public void activarRifa(Long rifaId) {

        Rifa rifa = rifaRepository.findById(rifaId)
                .orElseThrow();

        if (!rifa.getEstado().getNombre().equals("PENDIENTE_APROBACION")) {
                throw new RuntimeException("Solo rifas pendientes pueden activarse.");
        }

        List<RifaEmpresa> asociaciones =
                rifaEmpresaRepository.findByRifaId(rifaId);

        if (asociaciones.isEmpty()) {
                throw new RuntimeException(
                        "La rifa debe tener al menos una empresa asociada."
                );
        }

        EstadoRifa estadoActivo = estadoRifaRepository
                .findByNombre("ACTIVA")
                .orElseThrow();

        rifa.setEstado(estadoActivo);

        rifaRepository.save(rifa);
        }

        @Override
        public void enviarRevision(Long rifaId) {

        Rifa rifa = rifaRepository.findById(rifaId)
                .orElseThrow();

        if (!rifa.getEstado().getNombre().equals("PENDIENTE")) {

                throw new RuntimeException(
                        "Solo rifas pendientes pueden enviarse a revisión."
                );
        }

        List<RifaEmpresa> asociaciones =
                rifaEmpresaRepository.findByRifaId(rifaId);

        if (asociaciones.isEmpty()) {

                throw new RuntimeException(
                        "La rifa debe tener al menos una empresa asociada."
                );
        }

        EstadoRifa estado =
                estadoRifaRepository
                        .findByNombre("PENDIENTE_APROBACION")
                        .orElseThrow();

        rifa.setEstado(estado);

        rifaRepository.save(rifa);
        }

        @Override
        public void rechazarRifa(Long rifaId) {

        Rifa rifa = rifaRepository.findById(rifaId)
                .orElseThrow();

        EstadoRifa estado =
                estadoRifaRepository
                        .findByNombre("RECHAZADA")
                        .orElseThrow();

        if (!rifa.getEstado().getNombre().equals("PENDIENTE_APROBACION")) {
        throw new RuntimeException(
                "Solo rifas en revisión pueden rechazarse."
        );
        }

        rifa.setEstado(estado);

        rifaRepository.save(rifa);
        }

        @Override
        public List<RifaResponse> listarRifasPendientes() {

        return rifaRepository.findByEstadoNombre("PENDIENTE")
                .stream()
                .map(this::mapToResponse)
                .toList();
        }

        @Override
        public List<RifaResponse> listarRifasPendientesAprobacion() {

        return rifaRepository
                .findByEstadoNombre("PENDIENTE_APROBACION")
                .stream()
                .map(this::mapToResponse)
                .toList();
        }

        @Override
        public void asociarEmpresa(AsociarEmpresaRifaRequest request, Long empresaId) {

        Rifa rifa = rifaRepository.findById(request.getRifaId())
                .orElseThrow();

        if (!rifa.getEstado().getNombre().equals("PENDIENTE")) {
                throw new RuntimeException("Solo se pueden asociar empresas a rifas pendientes.");
        }

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow();

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow();

        if (!producto.getEmpresa().getId().equals(empresaId)) {
                throw new RuntimeException("El producto no pertenece a la empresa.");
        }

        boolean yaExiste = rifaEmpresaRepository
                .existsByRifaIdAndProductoId(
                        request.getRifaId(),
                        request.getProductoId()
                );

        if (yaExiste) {
                throw new RuntimeException("El producto ya fue asociado a esta rifa.");
        }

        RifaEmpresa rifaEmpresa = new RifaEmpresa();

        rifaEmpresa.setRifa(rifa);
        rifaEmpresa.setEmpresa(empresa);
        rifaEmpresa.setProducto(producto);

        rifaEmpresaRepository.save(rifaEmpresa);
        }

        @Override
        public List<RifaResponse> listarRifasActivas() {
        return rifaRepository.findRifasActivasVigentes()
                .stream()
                .map(this::mapToResponse)
                .toList();
        }

    @Override
    public RifaResponse obtenerDetalle(Long id) {

        Rifa rifa = rifaRepository.findById(id)
                .orElseThrow();

        return mapToResponse(rifa);
    }

    private RifaResponse mapToResponse(Rifa rifa) {

        RifaResponse response = new RifaResponse();

        response.setId(rifa.getId());
        response.setTitulo(rifa.getTitulo());
        response.setDescripcion(rifa.getDescripcion());

        response.setProgramaNombre(
                rifa.getPrograma().getNombre()
        );

        response.setOngNombre(
                rifa.getPrograma().getOng().getNombre()
        );

        response.setPrecioTicket(rifa.getPrecioTicket());
        response.setStockTickets(rifa.getStockTickets());
        response.setTicketsVendidos(rifa.getTicketsVendidos());
        response.setImagenUrl(rifa.getImagenUrl());
        response.setFechaFin(rifa.getFechaFin());

        response.setEstado(
                rifa.getEstado().getNombre()
        );

        double objetivo = rifa.getPrograma().getObjetivoMonto().doubleValue();

        double progreso = 0;

        if (objetivo > 0) {
        progreso = (
                rifa.getPrograma().getMontoActual().doubleValue()
                        / objetivo
        ) * 100;
        }

        response.setProgresoPrograma(progreso);

        double progresoRifa = 0;

        if (rifa.getStockTickets() > 0) {

        progresoRifa =
                (
                        rifa.getTicketsVendidos().doubleValue()
                        /
                        rifa.getStockTickets().doubleValue()
                ) * 100;
        }

        response.setProgresoRifa(progresoRifa);

        List<RifaEmpresaResponse> empresasAsociadas = rifaEmpresaRepository
                .findByRifaId(rifa.getId())
                .stream()
                .map(this::mapEmpresaResponse)
                .toList();

        response.setEmpresas(empresasAsociadas);

        response.setEmpresasAsociadas(
                empresasAsociadas.size()
        );

        return response;

    }

        private RifaEmpresaResponse mapEmpresaResponse(RifaEmpresa rifaEmpresa) {

        RifaEmpresaResponse response = new RifaEmpresaResponse();

        response.setEmpresaId(
                rifaEmpresa.getEmpresa().getId()
        );

        response.setEmpresaNombre(
                rifaEmpresa.getEmpresa().getNombre()
        );

        response.setProductoId(
                rifaEmpresa.getProducto().getId()
        );

        response.setProductoNombre(
                rifaEmpresa.getProducto().getNombre()
        );

        response.setProductoImagen(
                rifaEmpresa.getProducto().getImagenUrl()
        );

        return response;
        }

        @Override
        public List<RifaEmpresaResponse> listarEmpresasAsociadas(Long rifaId) {

        return rifaEmpresaRepository.findByRifaId(rifaId)
                .stream()
                .map(this::mapEmpresaResponse)
                .toList();
        }

        @Override
        public List<RifaResponse> listarPorEmpresa(Long empresaId) {

        return rifaEmpresaRepository
                .findByEmpresaId(empresaId)
                .stream()
                .map(RifaEmpresa::getRifa)
                .distinct()
                .map(this::mapToResponse)
                .toList();
        }

        @Override
        public List<RifaResponse> listarPorOng(Long ongId) {

        return rifaRepository.findByOngId(ongId)
                .stream()
                .map(this::mapToResponse)
                .toList();
        }

        @Override
        public List<RifaResponse> listarRifasRechazadas() {

        return rifaRepository
                .findByEstadoNombre("RECHAZADA")
                .stream()
                .map(this::mapToResponse)
                .toList();
        }

        @Override
        public List<RifaResponse> listarRifasActivas(String titulo) {

        List<Rifa> rifas;

        if (titulo == null || titulo.isBlank()) {

                rifas = rifaRepository.findRifasActivasVigentes();

        } else {

                rifas = rifaRepository
                        .findRifasActivasVigentesByTitulo(titulo);
        }

        return rifas.stream()
                .map(this::mapToResponse)
                .toList();
        }
}