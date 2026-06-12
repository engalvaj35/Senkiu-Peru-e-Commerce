package com.senkiu.programa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.senkiu.programa.service.ProgramaSocialService;
import com.senkiu.categoria.repository.CategoriaRepository;

@Controller
public class ProgramaSocialController {

	private final ProgramaSocialService programaSocialService;
	private final CategoriaRepository categoriaRepository;

	public ProgramaSocialController(ProgramaSocialService programaSocialService,
			CategoriaRepository categoriaRepository) {

		this.programaSocialService = programaSocialService;
		this.categoriaRepository = categoriaRepository;
	}

	@GetMapping("/programas")
	public String listarProgramas(@RequestParam(required = false) String nombre,
			@RequestParam(required = false) String categoria, Model model) {

		model.addAttribute("programas", programaSocialService.listarProgramasAprobados(nombre, categoria));

		model.addAttribute("categorias", categoriaRepository.findByTipo("PROGRAMA"));

		model.addAttribute("nombre", nombre);
		model.addAttribute("categoria", categoria);

		return "programas/lista";
	}
}