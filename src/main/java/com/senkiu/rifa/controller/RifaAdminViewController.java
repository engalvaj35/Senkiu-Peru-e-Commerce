package com.senkiu.rifa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/rifas")
public class RifaAdminViewController {

    @GetMapping
    public String rifasHome() {
        return "admin/rifas"; 
    }

    @GetMapping("/pendientes")
    public String pendientes() {

        return "admin/rifas/pendientes";
    }

    @GetMapping("/tickets")
    public String tickets(@RequestParam Long rifaId, org.springframework.ui.Model model) {
        model.addAttribute("rifaId", rifaId);
        return "admin/rifas/tickets";
    }
}