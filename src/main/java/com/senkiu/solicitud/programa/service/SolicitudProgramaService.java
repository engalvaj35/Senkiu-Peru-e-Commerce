package com.senkiu.solicitud.programa.service;
import java.util.List;
import java.util.Optional;
import com.senkiu.solicitud.programa.dto.SolicitudProgramaRequest;
import com.senkiu.solicitud.programa.model.SolicitudPrograma;
public interface SolicitudProgramaService {
 SolicitudPrograma crearSolicitud(Long ongId, SolicitudProgramaRequest
request);
 List<SolicitudPrograma> listarPorOng(Long ongId);

 List<SolicitudPrograma> listarTodas();
 Optional<SolicitudPrograma> obtenerPorId(Long id);
 SolicitudPrograma guardar(SolicitudPrograma solicitud);

 void aprobar(Long id);
 void rechazar(Long id);
}