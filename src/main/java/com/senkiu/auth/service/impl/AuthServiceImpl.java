package com.senkiu.auth.service.impl;

import com.senkiu.auth.dto.*;
import com.senkiu.usuario.repository.UsuarioRepository;
import com.senkiu.auth.service.AuthService;
import com.senkiu.usuario.model.EstadoUsuario;
import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.EstadoUsuarioRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EstadoUsuarioRepository estadoRepository;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            EstadoUsuarioRepository estadoRepository,
            AuthenticationManager authenticationManager
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.estadoRepository = estadoRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
        public void login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        }

    @Override
    public AuthResponse register(RegisterRequest request) {

        Optional<Usuario> existingUser = usuarioRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario user = new Usuario();

        user.setNombres(request.getNombres());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRol("ROLE_USER");

        EstadoUsuario estado = estadoRepository.findByNombre("ACTIVO")
                .orElseThrow(() -> new RuntimeException("Estado ACTIVO no encontrado"));

        user.setEstado(estado);

        usuarioRepository.save(user);

        return new AuthResponse(
                user.getEmail(),
                user.getRol()
        );
    }
}