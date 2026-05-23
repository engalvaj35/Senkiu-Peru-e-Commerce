package com.senkiu.admin.service;

import java.util.List;

import com.senkiu.admin.dto.UsuarioAdminDTO;

public interface AdminService {

    List<UsuarioAdminDTO> listarUsuarios();
    void cambiarRol(Long id, String rol);
    void suspenderUsuario(Long id);
    void activarUsuario(Long id);
}