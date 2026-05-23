package com.senkiu.solicitud.programa.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senkiu.categoria.model.Categoria;
import com.senkiu.categoria.repository.CategoriaRepository;
import com.senkiu.estado.publicacion.model.EstadoPublicacion;
import com.senkiu.estado.publicacion.repository.EstadoPublicacionRepository;
import com.senkiu.ong.model.Ong;
import com.senkiu.ong.repository.OngRepository;
import com.senkiu.programa.model.ProgramaSocial;
import com.senkiu.programa.repository.ProgramaSocialRepository;
import com.senkiu.solicitud.programa.dto.SolicitudProgramaRequest;
import com.senkiu.solicitud.programa.mapper.SolicitudProgramaMapper;
import com.senkiu.solicitud.programa.model.SolicitudPrograma;
import com.senkiu.solicitud.programa.repository.SolicitudProgramaRepository;
import com.senkiu.solicitud.programa.service.SolicitudProgramaService;

@Service
@Transactional
public class SolicitudProgramaServiceImpl
        implements SolicitudProgramaService {

    private final SolicitudProgramaRepository solicitudProgramaRepository;

    private final SolicitudProgramaMapper solicitudProgramaMapper;

    private final ProgramaSocialRepository programaSocialRepository;

    private final CategoriaRepository categoriaRepository;

    private final EstadoPublicacionRepository estadoPublicacionRepository;
    
    private final OngRepository ongRepository;

    public SolicitudProgramaServiceImpl(
            SolicitudProgramaRepository solicitudProgramaRepository,
            SolicitudProgramaMapper solicitudProgramaMapper,
            ProgramaSocialRepository programaSocialRepository,
            CategoriaRepository categoriaRepository,
            EstadoPublicacionRepository estadoPublicacionRepository,
            OngRepository ongRepository
    ) {

        this.solicitudProgramaRepository =
                solicitudProgramaRepository;

        this.solicitudProgramaMapper =
                solicitudProgramaMapper;

        this.programaSocialRepository =
                programaSocialRepository;

        this.categoriaRepository =
                categoriaRepository;

        this.estadoPublicacionRepository =
                estadoPublicacionRepository;

        this.ongRepository = 
                ongRepository;
    }

    @Override
    public SolicitudPrograma crearSolicitud(
            Long ongId,
            SolicitudProgramaRequest request
    ) {

        SolicitudPrograma solicitud =
                solicitudProgramaMapper.toEntity(request);

        // Obtener categoría enviada desde el formulario
        Categoria categoria = categoriaRepository
                .findById(request.getCategoriaId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Categoría no encontrada"
                        ));

        solicitud.setCategoria(categoria);

        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() ->
                        new RuntimeException("ONG no encontrada"));

        solicitud.setOng(ong);

        EstadoPublicacion estadoPendiente =
                estadoPublicacionRepository
                        .findByNombre("PENDIENTE")
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Estado PENDIENTE no encontrado"
                                ));

        solicitud.setEstado(estadoPendiente);

        return solicitudProgramaRepository.save(solicitud);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudPrograma> listarPorOng(Long ongId) {

        return solicitudProgramaRepository.findByOngIdAndTipo(ongId, "PROGRAMA");
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudPrograma> listarTodas() {

        return solicitudProgramaRepository.findByTipo("PROGRAMA");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SolicitudPrograma> obtenerPorId(Long id) {

        return solicitudProgramaRepository.findById(id);
    }

    @Override
    public SolicitudPrograma guardar(SolicitudPrograma solicitud) {

        return solicitudProgramaRepository.save(solicitud);
    }

    @Override
    public void aprobar(Long id) {

        SolicitudPrograma solicitud =
                solicitudProgramaRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Solicitud de programa no encontrada"
                                ));

        EstadoPublicacion estadoPublicado =
                estadoPublicacionRepository
                        .findByNombre("PUBLICADO")
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Estado PUBLICADO no encontrado"
                                ));

        // Crear Programa Social
        ProgramaSocial programa = new ProgramaSocial();

        programa.setOng(solicitud.getOng());

        // Usar categoría de la solicitud
        programa.setCategoria(
                solicitud.getCategoria()
        );

        programa.setNombre(
                solicitud.getTitulo()
        );

        programa.setDescripcion(
                solicitud.getDescripcion()
        );

        // Usar monto objetivo real de la solicitud
        programa.setObjetivoMonto(
                solicitud.getObjetivoMonto()
        );

        programa.setMontoActual(
                BigDecimal.ZERO
        );

        programa.setEstado(
                estadoPublicado
        );

        programa =
                programaSocialRepository.save(programa);

        // Actualizar solicitud
        solicitud.setReferenciaId(
                programa.getId()
        );

        solicitud.setEstado(
                estadoPublicado
        );

        solicitudProgramaRepository.save(solicitud);
    }

    @Override
    public void rechazar(Long id) {

        SolicitudPrograma solicitud =
                solicitudProgramaRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Solicitud de programa no encontrada"
                                ));

        EstadoPublicacion estadoRechazado =
                estadoPublicacionRepository
                        .findByNombre("RECHAZADO")
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Estado RECHAZADO no encontrado"
                                ));

        solicitud.setEstado(estadoRechazado);

        solicitudProgramaRepository.save(solicitud);
    }
}