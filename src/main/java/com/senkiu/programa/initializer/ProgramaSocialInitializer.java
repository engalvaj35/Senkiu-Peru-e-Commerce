package com.senkiu.programa.initializer;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import com.senkiu.categoria.model.Categoria;
import com.senkiu.categoria.repository.CategoriaRepository;
import jakarta.annotation.PostConstruct;

@Component
public class ProgramaSocialInitializer {
	private final CategoriaRepository categoriaRepository;

	public ProgramaSocialInitializer(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}

	@PostConstruct
	public void init() {
		List<String> categoriasPrograma = Arrays.asList("Educación", "Salud", "Alimentación", "Medio Ambiente",
				"Protección Animal", "Inclusión Social", "Vivienda", "Agua Potable");
		for (String nombreCategoria : categoriasPrograma) {
			boolean existe = categoriaRepository.findByNombreAndTipo(nombreCategoria, "PROGRAMA").isPresent();
			if (!existe) {
				Categoria categoria = new Categoria();
				categoria.setNombre(nombreCategoria);
				categoria.setTipo("PROGRAMA");
				categoriaRepository.save(categoria);
			}
		}
	}
}
