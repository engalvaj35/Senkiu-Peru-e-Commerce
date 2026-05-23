package com.senkiu.init;

import com.senkiu.estado.pago.model.EstadoPago;
import com.senkiu.estado.pago.repository.EstadoPagoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class PagoDataInitializer {

    private final EstadoPagoRepository estadoPagoRepository;

    public PagoDataInitializer(EstadoPagoRepository estadoPagoRepository) {
        this.estadoPagoRepository = estadoPagoRepository;
    }

    @PostConstruct
    public void init() {

        crearEstadoSiNoExiste("PENDIENTE");
        crearEstadoSiNoExiste("COMPLETADO");
        crearEstadoSiNoExiste("FALLIDO");
        crearEstadoSiNoExiste("REEMBOLSADO");
    }

    private void crearEstadoSiNoExiste(String nombre) {

        estadoPagoRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    EstadoPago estado = new EstadoPago();
                    estado.setNombre(nombre);
                    return estadoPagoRepository.save(estado);
                });
    }
}