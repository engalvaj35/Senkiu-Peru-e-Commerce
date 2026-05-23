package com.senkiu.programa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.senkiu.programa.service.ProgramaSocialService;

@Controller
public class ProgramaSocialController {

    private final ProgramaSocialService programaSocialService;

    public ProgramaSocialController(
            ProgramaSocialService programaSocialService) {

        this.programaSocialService = programaSocialService;
    }

    @GetMapping("/programas")
    public String listarProgramas(Model model) {

        model.addAttribute(
                "programas",
                programaSocialService.listarProgramasAprobados()
        );

        return "programas/lista";
    }
}