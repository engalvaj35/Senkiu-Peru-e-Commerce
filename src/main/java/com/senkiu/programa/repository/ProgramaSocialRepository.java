package com.senkiu.programa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.senkiu.programa.model.ProgramaSocial;

@Repository
public interface ProgramaSocialRepository extends JpaRepository<ProgramaSocial, Long> {

	List<ProgramaSocial> findByOngId(Long ongId);

	List<ProgramaSocial> findByCategoriaId(Long categoriaId);

	List<ProgramaSocial> findByEstadoNombre(String nombreEstado);

	List<ProgramaSocial> findByEstadoNombreAndNombreContainingIgnoreCase(String estado, String nombre);

	List<ProgramaSocial> findByEstadoNombreAndCategoriaNombre(String estado, String categoria);

	List<ProgramaSocial> findByEstadoNombreAndNombreContainingIgnoreCaseAndCategoriaNombre(String estado, String nombre,
			String categoria);
}