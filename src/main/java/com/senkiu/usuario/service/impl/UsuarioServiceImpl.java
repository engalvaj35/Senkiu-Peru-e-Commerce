package com.senkiu.usuario.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.senkiu.usuario.dto.CambiarPasswordRequest;
import com.senkiu.usuario.dto.UsuarioPerfilRequest;
import com.senkiu.usuario.dto.UsuarioPerfilResponse;
import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;
import com.senkiu.usuario.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioPerfilResponse completarPerfil(
            Long usuarioId,
            UsuarioPerfilRequest request
    ) {

        Usuario user = repository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        // VALIDAR EMAIL

        if (request.getEmail() != null &&
                !request.getEmail().isBlank()) {

            Optional<Usuario> usuarioEmail =
                    repository.findByEmail(request.getEmail());

            if (usuarioEmail.isPresent() &&
                    !usuarioEmail.get().getId().equals(user.getId())) {

                throw new RuntimeException(
                        "El correo ya está registrado"
                );
            }

            user.setEmail(request.getEmail());
        }

        // CAMPOS NORMALES

        user.setNombres(request.getNombres());
        user.setApellidoPaterno(request.getApellidoPaterno());
        user.setApellidoMaterno(request.getApellidoMaterno());
        user.setDireccion(request.getDireccion());
        user.setDistrito(request.getDistrito());
        user.setProvincia(request.getProvincia());
        user.setDepartamento(request.getDepartamento());

        Usuario usuarioGuardado =
                repository.save(user);

        return convertirDTO(usuarioGuardado);
    }

    @Override
    public void cambiarPassword(
            Long usuarioId,
            CambiarPasswordRequest request
    ) {

        Usuario user = repository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        // VALIDAR PASSWORD ACTUAL

        if (!passwordEncoder.matches(
                request.getPasswordActual(),
                user.getPassword()
        )) {

            throw new RuntimeException(
                    "La contraseña actual es incorrecta"
            );
        }

        // VALIDAR CONFIRMACIÓN

        if (!request.getNuevaPassword()
                .equals(request.getConfirmarPassword())) {

            throw new RuntimeException(
                    "Las contraseñas no coinciden"
            );
        }

        // VALIDAR DIFERENTE

        if (passwordEncoder.matches(
                request.getNuevaPassword(),
                user.getPassword()
        )) {

            throw new RuntimeException(
                    "La nueva contraseña no puede ser igual a la anterior"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNuevaPassword()
                )
        );

        repository.save(user);
    }

    @Override
    public UsuarioPerfilResponse obtenerPerfil(
            Long usuarioId
    ) {

        Usuario user = repository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        ));

        return convertirDTO(user);
    }

    private UsuarioPerfilResponse convertirDTO(
            Usuario user
    ) {

        UsuarioPerfilResponse dto =
                new UsuarioPerfilResponse();

        dto.setId(user.getId());
        dto.setNombres(user.getNombres());
        dto.setEmail(user.getEmail());
        dto.setRol(user.getRol());
        dto.setApellidoPaterno(user.getApellidoPaterno());
        dto.setApellidoMaterno(user.getApellidoMaterno());
        dto.setDni(user.getDni());
        dto.setCelular(user.getCelular());
        dto.setDireccion(user.getDireccion());
        dto.setDistrito(user.getDistrito());
        dto.setProvincia(user.getProvincia());
        dto.setDepartamento(user.getDepartamento());

        return dto;
    }
}