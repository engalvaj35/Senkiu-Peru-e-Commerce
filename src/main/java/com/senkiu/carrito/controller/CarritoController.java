package com.senkiu.carrito.controller;

import com.senkiu.carrito.dto.CarritoResponse;
import com.senkiu.carrito.service.CarritoService;
import com.senkiu.security.CustomUserDetails;
import com.senkiu.carrito.dto.CarritoRequest;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    // =========================
    // AGREGAR PRODUCTO
    // =========================

    @PostMapping("/agregar")
    public String agregarProducto(
            @Valid @ModelAttribute CarritoRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes
    ) {

        try {

            Long empresaId = carritoService.agregarProducto(
                    userDetails.getId(),
                    request.getProductoId(),
                    request.getCantidad()
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Producto agregado al carrito"
            );

            return "redirect:/carrito/" + empresaId;

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/productos";
        }
    }

    // =========================
    // VER CARRITO
    // =========================

    @GetMapping("/{empresaId}")
    public String obtenerCarrito(
            @PathVariable Long empresaId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            org.springframework.ui.Model model
    ) {

        CarritoResponse carrito =
                carritoService.obtenerCarrito(
                        userDetails.getId(),
                        empresaId
                );

        model.addAttribute("carrito", carrito);

        return "carrito/carrito";
    }

    // =========================
    // ELIMINAR ITEM
    // =========================

    @PostMapping("/eliminar-item/{itemId}")
    public String eliminarItem(@PathVariable Long itemId) {

        carritoService.eliminarItem(itemId);

        return "redirect:/";
    }

    // =========================
    // VACIAR CARRITO
    // =========================

    @PostMapping("/vaciar/{carritoId}")
    public String vaciarCarrito(@PathVariable Long carritoId) {

        carritoService.vaciarCarrito(carritoId);

        return "redirect:/";
    }
}