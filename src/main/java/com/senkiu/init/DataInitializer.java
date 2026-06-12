package com.senkiu.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.senkiu.usuario.model.EstadoUsuario;
import com.senkiu.usuario.repository.EstadoUsuarioRepository;
import com.senkiu.estado.organizacion.model.EstadoOrganizacion;
import com.senkiu.estado.organizacion.repository.EstadoOrganizacionRepository;
import com.senkiu.categoria.model.Categoria;
import com.senkiu.categoria.repository.CategoriaRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	private final EstadoUsuarioRepository estadoUsuarioRepository;
	private final EstadoOrganizacionRepository estadoOrganizacionRepository;
	private final CategoriaRepository categoriaRepository;

	public DataInitializer(EstadoUsuarioRepository estadoUsuarioRepository,
			EstadoOrganizacionRepository estadoOrganizacionRepository, CategoriaRepository categoriaRepository) {
		this.estadoUsuarioRepository = estadoUsuarioRepository;
		this.estadoOrganizacionRepository = estadoOrganizacionRepository;
		this.categoriaRepository = categoriaRepository;
	}

	@Override
	public void run(String... args) {

		crearEstadoUsuarioSiNoExiste("ACTIVO");
		crearEstadoUsuarioSiNoExiste("INACTIVO");

		crearEstadoOrganizacionSiNoExiste("PENDIENTE");
		crearEstadoOrganizacionSiNoExiste("APROBADO");
		crearEstadoOrganizacionSiNoExiste("RECHAZADO");
		crearEstadoOrganizacionSiNoExiste("SUSPENDIDO");

		crearCategoria("Salud", "ONG");
		crearCategoria("Educación", "ONG");
		crearCategoria("Medio Ambiente", "ONG");
		crearCategoria("Animales", "ONG");
	}

	private void crearEstadoUsuarioSiNoExiste(String nombre) {

		estadoUsuarioRepository.findByNombre(nombre).ifPresentOrElse(estado -> {
		}, () -> {
			EstadoUsuario nuevo = new EstadoUsuario();
			nuevo.setNombre(nombre);
			estadoUsuarioRepository.save(nuevo);
			System.out.println("Estado usuario creado: " + nombre);
		});
	}

	private void crearEstadoOrganizacionSiNoExiste(String nombre) {

		estadoOrganizacionRepository.findByNombre(nombre).ifPresentOrElse(estado -> {
		}, () -> {
			EstadoOrganizacion nuevo = new EstadoOrganizacion();
			nuevo.setNombre(nombre);
			estadoOrganizacionRepository.save(nuevo);
			System.out.println("Estado organización creado: " + nombre);
		});
	}

	private void crearCategoria(String nombre, String tipo) {

		categoriaRepository.findByNombreAndTipo(nombre, tipo).ifPresentOrElse(c -> {
		}, () -> {
			Categoria categoria = new Categoria();
			categoria.setNombre(nombre);
			categoria.setTipo(tipo);
			categoriaRepository.save(categoria);
			System.out.println("Categoría creada: " + nombre);
		});
	}
}