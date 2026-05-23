package com.senkiu.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.senkiu.estado.publicacion.model.EstadoPublicacion;
import com.senkiu.estado.publicacion.repository.EstadoPublicacionRepository;

import com.senkiu.categoria.model.Categoria;
import com.senkiu.categoria.repository.CategoriaRepository;

@Component
public class ProductoDataInitializer implements CommandLineRunner {

    private final EstadoPublicacionRepository estadoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoDataInitializer(
            EstadoPublicacionRepository estadoRepository,
            CategoriaRepository categoriaRepository
    ) {
        this.estadoRepository = estadoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(String... args) {

        // ESTADOS DE PRODUCTO
        crearEstadoPublicacion("PENDIENTE");
        crearEstadoPublicacion("PUBLICADO");
        crearEstadoPublicacion("RECHAZADO");
        crearEstadoPublicacion("SUSPENDIDO");

        // CATEGORÍAS PRODUCTO
        crearCategoriaProducto("Electrónica");
        crearCategoriaProducto("Ropa");
        crearCategoriaProducto("Hogar");
        crearCategoriaProducto("Deportes");
    }

    private void crearEstadoPublicacion(String nombre) {

        estadoRepository.findByNombre(nombre)
                .ifPresentOrElse(
                        e -> {},
                        () -> {
                            EstadoPublicacion estado = new EstadoPublicacion();
                            estado.setNombre(nombre);
                            estadoRepository.save(estado);
                        }
                );
    }

    private void crearCategoriaProducto(String nombre) {

        categoriaRepository.findByNombreAndTipo(nombre, "PRODUCTO")
                .ifPresentOrElse(
                        c -> {},
                        () -> {
                            Categoria categoria = new Categoria();
                            categoria.setNombre(nombre);
                            categoria.setTipo("PRODUCTO");
                            categoriaRepository.save(categoria);
                        }
                );
    }
}