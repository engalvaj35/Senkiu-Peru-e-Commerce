package com.senkiu.init;

import com.senkiu.estado.rifa.model.EstadoRifa;
import com.senkiu.estado.rifa.repository.EstadoRifaRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RifaEstadoInitializer implements CommandLineRunner {

	private final EstadoRifaRepository repository;

	public RifaEstadoInitializer(EstadoRifaRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... args) {
		crearEstado("PENDIENTE");
		crearEstado("PENDIENTE_APROBACION");
		crearEstado("ACTIVA");
		crearEstado("FINALIZADA");
		crearEstado("RECHAZADA");
	}

	private void crearEstado(String nombre) {

		if (repository.findByNombre(nombre).isEmpty()) {
			EstadoRifa estado = new EstadoRifa();

			estado.setNombre(nombre);

			repository.save(estado);
		}
	}
}