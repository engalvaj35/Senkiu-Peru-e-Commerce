package com.senkiu.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.senkiu.estado.entrega.model.EstadoEntrega;
import com.senkiu.estado.entrega.repository.EstadoEntregaRepository;

@Component
public class EntregaDataInitializer implements CommandLineRunner {

	private final EstadoEntregaRepository repository;

	public EntregaDataInitializer(EstadoEntregaRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... args) {
		crearEstado("PENDIENTE");
		crearEstado("EN_CAMINO");
		crearEstado("ENTREGADO");
		crearEstado("CANCELADO");
	}

	private void crearEstado(String nombre) {

		repository.findByNombre(nombre).ifPresentOrElse(e -> {
		}, () -> {
			EstadoEntrega estado = new EstadoEntrega();
			estado.setNombre(nombre);
			repository.save(estado);
		});
	}
}