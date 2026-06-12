package com.senkiu.solicitud.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.senkiu.solicitud.dto.SolicitudEmpresaRequest;
import com.senkiu.solicitud.dto.SolicitudOngRequest;
import com.senkiu.solicitud.model.SolicitudOrganizacion;
import com.senkiu.solicitud.repository.SolicitudOrganizacionRepository;
import com.senkiu.solicitud.service.SolicitudOrganizacionService;

import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;

import com.senkiu.estado.organizacion.model.EstadoOrganizacion;
import com.senkiu.estado.organizacion.repository.EstadoOrganizacionRepository;
import com.senkiu.ong.service.OngService;
import com.senkiu.storage.service.StorageService;
import com.senkiu.certificado.model.CertificadoOrganizacion;
import com.senkiu.certificado.repository.CertificadoRepository;
import com.senkiu.empresa.service.EmpresaService;
import com.senkiu.banco.repository.CuentaBancariaRepository;

import com.senkiu.empresa.model.Empresa;
import com.senkiu.ong.model.Ong;

import com.senkiu.banco.service.CuentaBancariaService;

import java.util.List;

@Service
public class SolicitudOrganizacionServiceImpl implements SolicitudOrganizacionService {

	private final SolicitudOrganizacionRepository repo;
	private final UsuarioRepository usuarioRepo;
	private final EstadoOrganizacionRepository estadoRepo;
	private final StorageService storageService;
	private final CertificadoRepository certificadoRepo;
	private final EmpresaService empresaService;
	private final OngService ongService;
	private final CuentaBancariaService cuentaBancariaService;
	private final CuentaBancariaRepository cuentaRepository;

	public SolicitudOrganizacionServiceImpl(SolicitudOrganizacionRepository repo, UsuarioRepository usuarioRepo,
			EstadoOrganizacionRepository estadoRepo, StorageService storageService,
			CertificadoRepository certificadoRepo, EmpresaService empresaService, OngService ongService,
			CuentaBancariaService cuentaBancariaService, CuentaBancariaRepository cuentaRepository) {
		this.repo = repo;
		this.usuarioRepo = usuarioRepo;
		this.estadoRepo = estadoRepo;
		this.storageService = storageService;
		this.certificadoRepo = certificadoRepo;
		this.empresaService = empresaService;
		this.ongService = ongService;
		this.cuentaBancariaService = cuentaBancariaService;
		this.cuentaRepository = cuentaRepository;
	}

	@Override
	public List<SolicitudOrganizacion> listarTodas() {
		return repo.findAll();
	}

	@Override
	public void crearSolicitudEmpresa(Long usuarioId, SolicitudEmpresaRequest request, MultipartFile archivo) {

		if (request.getNumeroCuenta() != null && !request.getNumeroCuenta().isBlank()
				&& cuentaRepository.existsByNumeroCuenta(request.getNumeroCuenta().trim())) {

			throw new RuntimeException("El número de cuenta ya está registrado");
		}

		if (request.getCci() != null && !request.getCci().isBlank()
				&& cuentaRepository.existsByCci(request.getCci().trim())) {

			throw new RuntimeException("El CCI ya está registrado");
		}

		Usuario user = usuarioRepo.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no existe"));

		EstadoOrganizacion estado = estadoRepo.findByNombre("PENDIENTE").orElseThrow();

		SolicitudOrganizacion sol = new SolicitudOrganizacion();
		sol.setUsuario(user);
		sol.setNombre(request.getNombre());
		sol.setRuc(request.getRuc());
		sol.setEmail(request.getEmail());
		sol.setDireccion(request.getDireccion());
		sol.setEstado(estado);
		sol.setTipo("EMPRESA");
		sol.setDatosJson("""
				{
				"titularCuenta": "%s",
				"banco": "%s",
				"tipoCuenta": "%s",
				"numeroCuenta": "%s",
				"cci": "%s",
				"moneda": "%s"
				}
				""".formatted(request.getTitularCuenta(), request.getBanco(), request.getTipoCuenta(),
				request.getNumeroCuenta(), request.getCci(), request.getMoneda()));

		repo.save(sol);

		guardarCertificado(sol, request.getNombreCertificado(), archivo);
	}

	@Override
	public void crearSolicitudOng(Long usuarioId, SolicitudOngRequest request, MultipartFile archivo) {

		if (request.getNumeroCuenta() != null && !request.getNumeroCuenta().isBlank()
				&& cuentaRepository.existsByNumeroCuenta(request.getNumeroCuenta().trim())) {

			throw new RuntimeException("El número de cuenta ya está registrado");
		}

		if (request.getCci() != null && !request.getCci().isBlank()
				&& cuentaRepository.existsByCci(request.getCci().trim())) {

			throw new RuntimeException("El CCI ya está registrado");
		}

		Usuario user = usuarioRepo.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no existe"));

		EstadoOrganizacion estado = estadoRepo.findByNombre("PENDIENTE").orElseThrow();

		SolicitudOrganizacion sol = new SolicitudOrganizacion();

		sol.setUsuario(user);
		sol.setNombre(request.getNombre());
		sol.setRuc(request.getRuc());
		sol.setEmail(request.getEmail());
		sol.setDireccion(request.getDireccion());
		sol.setEstado(estado);
		sol.setTipo("ONG");

		sol.setDatosJson("""
				{
				"descripcion": "%s",
				"razonSocial": "%s",
				"categoriaId": %d,
				"titularCuenta": "%s",
				"banco": "%s",
				"tipoCuenta": "%s",
				"numeroCuenta": "%s",
				"cci": "%s",
				"moneda": "%s"
				}
				""".formatted(request.getDescripcion(), request.getRazonSocial(), request.getCategoriaId(),
				request.getTitularCuenta(), request.getBanco(), request.getTipoCuenta(), request.getNumeroCuenta(),
				request.getCci(), request.getMoneda()));

		repo.save(sol);

		guardarCertificado(sol, request.getNombreCertificado(), archivo);
	}

	@Override
	public void aprobarSolicitud(Long id) {

		SolicitudOrganizacion sol = repo.findById(id).orElseThrow();

		if (!sol.getEstado().getNombre().equals("PENDIENTE")) {
			throw new RuntimeException("Solo pendientes");
		}

		EstadoOrganizacion aprobado = estadoRepo.findByNombre("APROBADO").orElseThrow();

		sol.setEstado(aprobado);
		repo.save(sol);

		if (sol.getTipo().equals("EMPRESA")) {
			Empresa empresa = empresaService.crearDesdeSolicitud(sol);

			cuentaBancariaService.crearDesdeSolicitudEmpresa(empresa.getId(), sol);
		}

		if (sol.getTipo().equals("ONG")) {
			Ong ong = ongService.crearDesdeSolicitud(sol);

			cuentaBancariaService.crearDesdeSolicitudOng(ong.getId(), sol);
		}
	}

	@Override
	public void rechazarSolicitud(Long id) {

		SolicitudOrganizacion sol = repo.findById(id).orElseThrow();

		if (!sol.getEstado().getNombre().equals("PENDIENTE")) {
			throw new RuntimeException("Solo solicitudes pendientes pueden rechazarse");
		}

		EstadoOrganizacion rechazo = estadoRepo.findByNombre("RECHAZADO").orElseThrow();

		sol.setEstado(rechazo);
		repo.save(sol);
	}

	private void guardarCertificado(SolicitudOrganizacion sol, String nombreCertificado, MultipartFile archivo) {

		String url = storageService.guardarArchivo(archivo);

		CertificadoOrganizacion cert = new CertificadoOrganizacion();
		cert.setSolicitud(sol);
		cert.setNombre(nombreCertificado);
		cert.setArchivoUrl(url);

		certificadoRepo.save(cert);
	}
}