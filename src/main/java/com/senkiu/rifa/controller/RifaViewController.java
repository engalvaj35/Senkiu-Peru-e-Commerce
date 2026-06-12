package com.senkiu.rifa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.ui.Model;

@Controller
public class RifaViewController {

    @GetMapping("/rifas")
    public String rifas() {
        return "rifa/lista";
    }

    @GetMapping("/rifas/{id}")
    public String detalle(
            @PathVariable Long id,
            Model model
    ) {

        model.addAttribute("rifaId", id);

        return "rifa/detalle";
    }
}