package com.senkiu.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.senkiu.estado.orden.model.EstadoOrden;
import com.senkiu.estado.orden.repository.EstadoOrdenRepository;

@Component
public class OrdenDataInitializer implements CommandLineRunner {

    private final EstadoOrdenRepository estadoOrdenRepository;

    public OrdenDataInitializer(
            EstadoOrdenRepository estadoOrdenRepository
    ) {
        this.estadoOrdenRepository = estadoOrdenRepository;
    }

    @Override
    public void run(String... args) {

        crearEstado("PENDIENTE");
        crearEstado("ATENDIDO");
        crearEstado("CANCELADO");
        crearEstado("COMPLETADO");
    }

    private void crearEstado(String nombre) {

        estadoOrdenRepository
                .findByNombre(nombre)
                .ifPresentOrElse(
                        e -> {},
                        () -> {

                            EstadoOrden estado =
                                    new EstadoOrden();

                            estado.setNombre(nombre);

                            estadoOrdenRepository.save(estado);
                        }
                );
    }
}