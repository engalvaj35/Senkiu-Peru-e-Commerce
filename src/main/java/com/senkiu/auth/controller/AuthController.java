package com.senkiu.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.senkiu.auth.dto.RegisterRequest;
import com.senkiu.auth.service.AuthService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

	private final AuthService service;

	public AuthController(AuthService service) {
		this.service = service;
	}

	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("registerRequest", new RegisterRequest());
		return "auth/register";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest request, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			return "auth/register";
		}

		try {
			service.register(request);
			return "redirect:/login-page?registered=true";

		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			return "auth/register";
		}
	}
}