package com.senkiu.programa.mapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;
import com.senkiu.programa.dto.ProgramaSocialDTO;
import com.senkiu.programa.model.ProgramaSocial;
@Component
public class ProgramaSocialMapper {
 public ProgramaSocialDTO toDTO(ProgramaSocial programa) {
 if (programa == null) {
 return null;
 }
 BigDecimal porcentajeAvance = BigDecimal.ZERO;
 if (programa.getObjetivoMonto() != null
 && programa.getObjetivoMonto().compareTo(BigDecimal.ZERO) >
0
 && programa.getMontoActual() != null) {
 porcentajeAvance = programa.getMontoActual()
 .multiply(BigDecimal.valueOf(100))
 .divide(programa.getObjetivoMonto(), 2,
RoundingMode.HALF_UP);
 }
 ProgramaSocialDTO dto = new ProgramaSocialDTO();
 dto.setId(programa.getId());
 dto.setNombre(programa.getNombre());
 dto.setDescripcion(programa.getDescripcion());
 dto.setObjetivoMonto(programa.getObjetivoMonto());
 dto.setMontoActual(programa.getMontoActual());
 dto.setPorcentajeAvance(porcentajeAvance);
 if (programa.getCategoria() != null) {
 dto.setCategoriaNombre(programa.getCategoria().getNombre());
 }
 if (programa.getEstado() != null) {
 dto.setEstadoNombre(programa.getEstado().getNombre());
 }
 return dto;
 }
}
