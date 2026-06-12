package com.senkiu.carrito.controller;

import com.senkiu.carrito.dto.CarritoRequest;
import com.senkiu.carrito.dto.CarritoResponse;
import com.senkiu.carrito.service.CarritoService;
import com.senkiu.security.CustomUserDetails;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping("/agregar")
    public String agregarProducto(
            @Valid @ModelAttribute CarritoRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {

            Long usuarioId = (userDetails != null) ? userDetails.getId() : null;
            String sessionId = session.getId();

            Long empresaId = carritoService.agregarProducto(
                    usuarioId,
                    sessionId,
                    request.getProductoId(),
                    request.getCantidad()
            );

            redirectAttributes.addFlashAttribute("success", "Producto agregado al carrito");

            return "redirect:/carrito/" + empresaId;

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/productos";
        }
    }

    @GetMapping("/{empresaId}")
    public String obtenerCarrito(
            @PathVariable Long empresaId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpSession session,
            Model model) {

        Long usuarioId = (userDetails != null) ? userDetails.getId() : null;
        String sessionId = session.getId();

        CarritoResponse carrito = carritoService.obtenerCarrito(usuarioId, sessionId, empresaId);

        model.addAttribute("carrito", carrito);

        return "carrito/carrito";
    }

    @PostMapping("/eliminar-item/{itemId}")
    public String eliminarItem(@PathVariable Long itemId) {
        carritoService.eliminarItem(itemId);
        return "redirect:/productos";
    }

    @PostMapping("/vaciar/{carritoId}")
    public String vaciarCarrito(@PathVariable Long carritoId) {
        carritoService.vaciarCarrito(carritoId);
        return "redirect:/productos";
    }
}