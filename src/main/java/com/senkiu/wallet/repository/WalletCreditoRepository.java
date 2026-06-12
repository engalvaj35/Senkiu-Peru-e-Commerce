package com.senkiu.wallet.repository;

import com.senkiu.wallet.model.WalletCredito;
import com.senkiu.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletCreditoRepository extends JpaRepository<WalletCredito, Long> {

    Optional<WalletCredito> findByUsuario(Usuario usuario);
}