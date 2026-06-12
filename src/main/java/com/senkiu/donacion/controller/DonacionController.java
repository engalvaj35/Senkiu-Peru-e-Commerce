package com.senkiu.donacion.controller;

import com.senkiu.donacion.dto.DonacionRequest;
import com.senkiu.donacion.service.DonacionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donaciones")
public class DonacionController {

    private final DonacionService donacionService;

    public DonacionController(DonacionService donacionService) {
        this.donacionService = donacionService;
    }

    @PostMapping("/{usuarioId}")
    public void donar(
            @PathVariable Long usuarioId,
            @ModelAttribute DonacionRequest request
    ) {
        donacionService.donar(usuarioId, request);
    }
}