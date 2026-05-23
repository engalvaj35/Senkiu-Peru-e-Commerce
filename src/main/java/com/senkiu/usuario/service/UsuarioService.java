package com.senkiu.usuario.service;

import com.senkiu.usuario.dto.UsuarioPerfilRequest;
import com.senkiu.usuario.dto.UsuarioPerfilResponse;
import com.senkiu.usuario.dto.CambiarPasswordRequest;

public interface UsuarioService {

    UsuarioPerfilResponse completarPerfil(
            Long usuarioId,
            UsuarioPerfilRequest request
    );

    UsuarioPerfilResponse obtenerPerfil(Long usuarioId);

    void cambiarPassword(
            Long usuarioId,
            CambiarPasswordRequest request
    );
}