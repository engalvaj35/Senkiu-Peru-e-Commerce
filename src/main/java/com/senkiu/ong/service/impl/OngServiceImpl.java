package com.senkiu.ong.service.impl;

import com.senkiu.ong.model.Ong;
import com.senkiu.ong.repository.OngRepository;
import com.senkiu.ong.service.OngService;

import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;

import com.senkiu.estado.organizacion.model.EstadoOrganizacion;
import com.senkiu.estado.organizacion.repository.EstadoOrganizacionRepository;

import com.senkiu.solicitud.model.SolicitudOrganizacion;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OngServiceImpl implements OngService {

    private final OngRepository ongRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoOrganizacionRepository estadoRepo;

    public OngServiceImpl(
            OngRepository ongRepository,
            UsuarioRepository usuarioRepository,
            EstadoOrganizacionRepository estadoRepo
    ) {
        this.ongRepository = ongRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoRepo = estadoRepo;
    }

    @Override
    public Ong obtenerPorUsuario(Long usuarioId) {

        Usuario user = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        return ongRepository.findByUsuario(user)
                .orElseThrow(() -> new RuntimeException("ONG no existe"));
    }

    @Override
    public Ong crearDesdeSolicitud(SolicitudOrganizacion sol) {

        EstadoOrganizacion aprobado = estadoRepo.findByNombre("APROBADO")
                .orElseThrow();

        Ong ong = new Ong();

        ong.setNombre(sol.getNombre());
        ong.setRuc(sol.getRuc());
        ong.setEmail(sol.getEmail());
        ong.setDireccion(sol.getDireccion());
        ong.setUsuario(sol.getUsuario());
        ong.setEstado(aprobado);

        // SOLO ONG tiene info extendida
        ong.setDescripcion(sol.getDatosJson());

        Ong saved = ongRepository.save(ong);

        // 🔥 CAMBIO DE ROL AQUÍ
        Usuario user = sol.getUsuario();
        user.setRol("ROLE_ONG");
        usuarioRepository.save(user);

        return saved;
        }

    @Override
    public void suspender(Long ongId) {

        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new RuntimeException("ONG no existe"));

        EstadoOrganizacion estado = estadoRepo.findByNombre("SUSPENDIDO")
                .orElseThrow();

        ong.setEstado(estado);

        ongRepository.save(ong);
    }

    @Override
    public void activar(Long ongId) {

        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new RuntimeException("ONG no existe"));

        EstadoOrganizacion estado = estadoRepo.findByNombre("APROBADO")
                .orElseThrow();

        ong.setEstado(estado);

        ongRepository.save(ong);
    }

    @Override
    public List<Ong> findAll() {
        return ongRepository.findAll();
    }
}