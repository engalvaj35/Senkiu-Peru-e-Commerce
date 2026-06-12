package com.senkiu.solicitud.programa.mapper;

import org.springframework.stereotype.Component;

import com.senkiu.solicitud.programa.dto.SolicitudProgramaRequest;
import com.senkiu.solicitud.programa.model.SolicitudPrograma;

@Component
public class SolicitudProgramaMapper {

    public SolicitudPrograma toEntity(SolicitudProgramaRequest request) {

        SolicitudPrograma solicitud = new SolicitudPrograma();

        solicitud.setTipo("PROGRAMA");
        solicitud.setTitulo(request.getTitulo());
        solicitud.setDescripcion(request.getDescripcion());

        solicitud.setObjetivoMonto(request.getObjetivoMonto());

        return solicitud;
    }
}