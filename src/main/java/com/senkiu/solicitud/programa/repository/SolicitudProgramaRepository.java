package com.senkiu.solicitud.programa.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.senkiu.solicitud.programa.model.SolicitudPrograma;
@Repository
public interface SolicitudProgramaRepository extends
JpaRepository<SolicitudPrograma, Long> {
 List<SolicitudPrograma> findByOngId(Long ongId);
 List<SolicitudPrograma> findByEstadoNombre(String nombreEstado);
 List<SolicitudPrograma> findByTipo(String tipo);
 List<SolicitudPrograma> findByOngIdAndTipo(Long ongId, String tipo);
}