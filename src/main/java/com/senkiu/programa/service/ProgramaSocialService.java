package com.senkiu.programa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.senkiu.programa.dto.ProgramaSocialDTO;
import com.senkiu.programa.model.ProgramaSocial;

public interface ProgramaSocialService {

	List<ProgramaSocialDTO> listarPorOng(Long ongId);

	List<ProgramaSocial> listarTodos();

	Optional<ProgramaSocialDTO> obtenerPorId(Long id);

	ProgramaSocial guardar(ProgramaSocial programa);

	void eliminar(Long id);

	List<ProgramaSocialDTO> listarProgramasAprobados();

	List<ProgramaSocialDTO> listarProgramasAprobados(String categoria);

	List<ProgramaSocialDTO> listarProgramasAprobados(String nombre, String categoria);

	void incrementarMonto(Long programaId, BigDecimal monto);
}