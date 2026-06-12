package com.senkiu.init;

import com.senkiu.estado.suscripcion.model.EstadoSuscripcion;
import com.senkiu.estado.suscripcion.repository.EstadoSuscripcionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SuscripcionDataInitializer implements CommandLineRunner {

	private final EstadoSuscripcionRepository repository;

	public SuscripcionDataInitializer(EstadoSuscripcionRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... args) {
		crear("PENDIENTE");
		crear("ACTIVA");
		crear("EXPIRADA");
		crear("CANCELADA");
	}

	private void crear(String nombre) {
		repository.findByNombre(nombre).ifPresentOrElse(e -> {
		}, () -> {
			EstadoSuscripcion estado = new EstadoSuscripcion();
			estado.setNombre(nombre);
			repository.save(estado);
		});
	}
}