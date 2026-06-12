package com.senkiu.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.senkiu.producto.service.ProductoService;

@Controller
public class ViewController {

    private final ProductoService productoService;

    public ViewController(
            ProductoService productoService
    ) {
        this.productoService = productoService;
    }

    @GetMapping("/register-page")
    public String registerPage() {
        return "auth/register";
    }

    @GetMapping("/login-page")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute(
                "productos",
                productoService.listarPublicados()
        );

        return "index";
    }

    @GetMapping("/sobre-nosotros")
    public String SobreNosotros() {
        return "about-me";
    }
    
}