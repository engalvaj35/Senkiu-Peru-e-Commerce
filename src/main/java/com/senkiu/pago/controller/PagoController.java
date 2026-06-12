package com.senkiu.pago.controller;

import com.senkiu.pago.dto.CrearPagoRequest;
import com.senkiu.pago.model.Pago;
import com.senkiu.pago.repository.PagoRepository;
import com.senkiu.pago.service.PagoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final PagoRepository pagoRepository;

    public PagoController(
            PagoService pagoService,
            PagoRepository pagoRepository
    ) {
        this.pagoService = pagoService;
        this.pagoRepository = pagoRepository;
    }

    @PostMapping
    @ResponseBody
    public Pago crear(
            @RequestBody CrearPagoRequest request
    ) {
        return pagoService.crearPago(request);
    }

    @GetMapping("/confirmar/{pagoId}")
    public String vistaConfirmar(
            @PathVariable Long pagoId,
            Model model
    ) {

        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() ->
                        new RuntimeException("Pago no encontrado"));

        model.addAttribute("pago", pago);

        return "pago/mock-pago";
    }

    @PostMapping("/confirmar/{pagoId}")
    public String confirmar(
            @PathVariable Long pagoId
    ) {

        pagoService.confirmarPago(pagoId);

        return "redirect:/";
    }
}