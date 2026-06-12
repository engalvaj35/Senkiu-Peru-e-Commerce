package com.senkiu.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.senkiu.util.EstadoFormatter;

@ControllerAdvice
public class GlobalModelAttributes {

	private final EstadoFormatter estadoFormatter;

	public GlobalModelAttributes(EstadoFormatter estadoFormatter) {
		this.estadoFormatter = estadoFormatter;
	}

	@ModelAttribute("estadoFormatter")
	public EstadoFormatter estadoFormatter() {
		return estadoFormatter;
	}
}