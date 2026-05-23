package com.senkiu.empresa.service.impl;

import com.senkiu.empresa.model.Empresa;
import com.senkiu.empresa.repository.EmpresaRepository;
import com.senkiu.empresa.service.EmpresaService;

import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;

import com.senkiu.estado.organizacion.model.EstadoOrganizacion;
import com.senkiu.estado.organizacion.repository.EstadoOrganizacionRepository;

import com.senkiu.solicitud.model.SolicitudOrganizacion;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoOrganizacionRepository estadoRepo;

    public EmpresaServiceImpl(
            EmpresaRepository repository,
            UsuarioRepository usuarioRepository,
            EstadoOrganizacionRepository estadoRepo
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.estadoRepo = estadoRepo;
    }

    @Override
    public Empresa obtenerPorUsuario(Long usuarioId) {

        return repository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Empresa no existe"));
    }

    @Override
    public Empresa crearDesdeSolicitud(SolicitudOrganizacion sol) {

        EstadoOrganizacion aprobado = estadoRepo.findByNombre("APROBADO")
                .orElseThrow();

        Empresa empresa = new Empresa();

        empresa.setNombre(sol.getNombre());
        empresa.setRuc(sol.getRuc());
        empresa.setEmail(sol.getEmail());
        empresa.setDireccion(sol.getDireccion());
        empresa.setUsuario(sol.getUsuario());
        empresa.setEstado(aprobado);

        Empresa saved = repository.save(empresa);

        Usuario user = sol.getUsuario();
        user.setRol("ROLE_EMPRESA");
        usuarioRepository.save(user);

        return saved;
        }

    @Override
    public void suspender(Long empresaId) {

        Empresa empresa = repository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa no existe"));

        EstadoOrganizacion estado = estadoRepo.findByNombre("SUSPENDIDO")
                .orElseThrow();

        empresa.setEstado(estado);

        repository.save(empresa);
    }

    @Override
    public void activar(Long empresaId) {

        Empresa empresa = repository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa no existe"));

        EstadoOrganizacion estado = estadoRepo.findByNombre("APROBADO")
                .orElseThrow();

        empresa.setEstado(estado);

        repository.save(empresa);
    }

    @Override
    public List<Empresa> findAll() {
        return repository.findAll();
    }
}