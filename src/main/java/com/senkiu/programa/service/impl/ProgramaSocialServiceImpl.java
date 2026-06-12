package com.senkiu.programa.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.senkiu.programa.dto.ProgramaSocialDTO;
import com.senkiu.programa.mapper.ProgramaSocialMapper;
import com.senkiu.programa.model.ProgramaSocial;
import com.senkiu.programa.repository.ProgramaSocialRepository;
import com.senkiu.programa.service.ProgramaSocialService;

@Service
@Transactional
public class ProgramaSocialServiceImpl implements ProgramaSocialService {
	private final ProgramaSocialRepository programaSocialRepository;
	private final ProgramaSocialMapper programaSocialMapper;

	public ProgramaSocialServiceImpl(ProgramaSocialRepository programaSocialRepository,
			ProgramaSocialMapper programaSocialMapper) {
		this.programaSocialRepository = programaSocialRepository;
		this.programaSocialMapper = programaSocialMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProgramaSocialDTO> listarPorOng(Long ongId) {
		List<ProgramaSocial> programas = programaSocialRepository.findByOngId(ongId);
		List<ProgramaSocialDTO> resultado = new ArrayList<>();
		for (ProgramaSocial programa : programas) {
			resultado.add(programaSocialMapper.toDTO(programa));
		}
		return resultado;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProgramaSocial> listarTodos() {
		return programaSocialRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ProgramaSocialDTO> obtenerPorId(Long id) {
		Optional<ProgramaSocial> programaOpt = programaSocialRepository.findById(id);
		if (programaOpt.isPresent()) {
			ProgramaSocialDTO dto = programaSocialMapper.toDTO(programaOpt.get());
			return Optional.of(dto);
		}
		return Optional.empty();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProgramaSocialDTO> listarProgramasAprobados(String categoria) {

		List<ProgramaSocial> programas;

		if (categoria == null || categoria.isBlank()) {

			programas = programaSocialRepository.findByEstadoNombre("PUBLICADO");

		} else {

			programas = programaSocialRepository.findByEstadoNombreAndCategoriaNombre("PUBLICADO", categoria);
		}

		List<ProgramaSocialDTO> resultado = new ArrayList<>();

		for (ProgramaSocial programa : programas) {
			resultado.add(programaSocialMapper.toDTO(programa));
		}

		return resultado;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProgramaSocialDTO> listarProgramasAprobados(String nombre, String categoria) {

		List<ProgramaSocial> programas;

		boolean tieneNombre = nombre != null && !nombre.isBlank();

		boolean tieneCategoria = categoria != null && !categoria.isBlank();

		if (!tieneNombre && !tieneCategoria) {

			programas = programaSocialRepository.findByEstadoNombre("PUBLICADO");

		} else if (tieneNombre && !tieneCategoria) {

			programas = programaSocialRepository.findByEstadoNombreAndNombreContainingIgnoreCase("PUBLICADO", nombre);

		} else if (!tieneNombre) {

			programas = programaSocialRepository.findByEstadoNombreAndCategoriaNombre("PUBLICADO", categoria);

		} else {

			programas = programaSocialRepository
					.findByEstadoNombreAndNombreContainingIgnoreCaseAndCategoriaNombre("PUBLICADO", nombre, categoria);
		}

		List<ProgramaSocialDTO> resultado = new ArrayList<>();

		for (ProgramaSocial programa : programas) {
			resultado.add(programaSocialMapper.toDTO(programa));
		}

		return resultado;
	}

	@Override
	public ProgramaSocial guardar(ProgramaSocial programa) {
		if (programa.getMontoActual() == null) {
			programa.setMontoActual(BigDecimal.ZERO);
		}
		return programaSocialRepository.save(programa);
	}

	@Override
	public void eliminar(Long id) {
		programaSocialRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProgramaSocialDTO> listarProgramasAprobados() {

		List<ProgramaSocial> programas = programaSocialRepository.findByEstadoNombre("PUBLICADO");

		List<ProgramaSocialDTO> resultado = new ArrayList<>();

		for (ProgramaSocial programa : programas) {
			resultado.add(programaSocialMapper.toDTO(programa));
		}

		return resultado;
	}

	@Override
	@Transactional
	public void incrementarMonto(Long programaId, BigDecimal monto) {

		ProgramaSocial programa = programaSocialRepository.findById(programaId)
				.orElseThrow(() -> new RuntimeException("Programa no existe"));

		BigDecimal actual = programa.getMontoActual() == null ? BigDecimal.ZERO : programa.getMontoActual();

		programa.setMontoActual(actual.add(monto));

		if (programa.getMontoActual().compareTo(programa.getObjetivoMonto()) >= 0) {
		}

		programaSocialRepository.save(programa);
	}
}