package com.senkiu.banco.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senkiu.banco.model.CuentaBancaria;

public interface CuentaBancariaRepository
        extends JpaRepository<CuentaBancaria, Long> {

    List<CuentaBancaria> findByEmpresaId(Long empresaId);

    List<CuentaBancaria> findByOngId(Long ongId);

    boolean existsByNumeroCuenta(String numeroCuenta);

    boolean existsByCci(String cci);
}