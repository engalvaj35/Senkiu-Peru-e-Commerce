package com.senkiu.admin.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.senkiu.admin.service.AdminService;
import com.senkiu.usuario.model.Usuario;
import com.senkiu.admin.dto.UsuarioAdminDTO;
import com.senkiu.usuario.repository.UsuarioRepository;
import com.senkiu.usuario.model.EstadoUsuario;
import com.senkiu.usuario.repository.EstadoUsuarioRepository;

@Service
public class AdminServiceImpl implements AdminService {

    private final UsuarioRepository usuarioRepository;
    private final EstadoUsuarioRepository estadoRepository;

    private static final Set<String> ROLES_VALIDOS = Set.of(
            "ROLE_USER",
            "ROLE_ADMIN",
            "ROLE_ONG",
            "ROLE_EMPRESA"
    );

    private UsuarioAdminDTO convertirDTO(Usuario user) {

        UsuarioAdminDTO dto = new UsuarioAdminDTO();

        dto.setId(user.getId());
        dto.setNombres(user.getNombres());
        dto.setEmail(user.getEmail());
        dto.setRol(user.getRol());

        if (user.getEstado() != null) {
            dto.setEstado(user.getEstado().getNombre());
        }

        return dto;
    }

    public AdminServiceImpl(
        UsuarioRepository usuarioRepository,
        EstadoUsuarioRepository estadoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.estadoRepository = estadoRepository;
    }

    @Override
    public List<UsuarioAdminDTO> listarUsuarios() {

        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Override
    public void cambiarRol(Long id, String rol) {

        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        rol = rol.toUpperCase().replace("ROLE_", "");
        rol = "ROLE_" + rol;

        if (!ROLES_VALIDOS.contains(rol)) {
            throw new RuntimeException("Rol no válido: " + rol);
        }

        user.setRol(rol);
        usuarioRepository.save(user);
    }

    @Override
    public void suspenderUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        EstadoUsuario estado = estadoRepository
                .findByNombre("INACTIVO")
                .orElseThrow(() ->
                        new RuntimeException("Estado INACTIVO no encontrado"));

        usuario.setEstado(estado);

        usuarioRepository.save(usuario);
    }

    @Override
    public void activarUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        EstadoUsuario estado = estadoRepository
                .findByNombre("ACTIVO")
                .orElseThrow(() ->
                        new RuntimeException("Estado ACTIVO no encontrado"));

        usuario.setEstado(estado);

        usuarioRepository.save(usuario);
    }
}
