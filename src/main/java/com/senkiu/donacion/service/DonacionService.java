package com.senkiu.donacion.service;

import com.senkiu.donacion.dto.DonacionRequest;
import com.senkiu.donacion.model.Donacion;

import java.util.List;

public interface DonacionService {

    Donacion donar(Long usuarioId, DonacionRequest request);

    Donacion buscarPorId(Long donacionId);

    List<Donacion> listarPorUsuario(Long usuarioId);

    List<Donacion> listarTodas();
}