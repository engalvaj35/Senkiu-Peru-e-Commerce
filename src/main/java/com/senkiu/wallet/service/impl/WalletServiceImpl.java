package com.senkiu.wallet.service.impl;

import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;
import com.senkiu.wallet.exception.SaldoInsuficienteException;
import com.senkiu.wallet.model.WalletCredito;
import com.senkiu.wallet.repository.WalletCreditoRepository;
import com.senkiu.wallet.service.WalletService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletCreditoRepository walletRepository;
    private final UsuarioRepository usuarioRepository;

    public WalletServiceImpl(
            WalletCreditoRepository walletRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.walletRepository = walletRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public void crearWalletSiNoExiste(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        walletRepository.findByUsuario(usuario)
                .ifPresentOrElse(
                        w -> {},
                        () -> {
                            WalletCredito wallet = new WalletCredito();
                            wallet.setUsuario(usuario);
                            wallet.setSaldo(BigDecimal.ZERO);
                            walletRepository.save(wallet);
                        }
                );
    }

    @Override
    @Transactional
    public void sumarCreditos(Long usuarioId, BigDecimal monto) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        WalletCredito wallet = walletRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Wallet no existe"));

        wallet.setSaldo(wallet.getSaldo().add(monto));

        walletRepository.save(wallet);
    }

        @Override
        @Transactional
        public void restarCreditos(
                Long usuarioId,
                BigDecimal monto
        ) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        WalletCredito wallet = walletRepository
                .findByUsuario(usuario)
                .orElseThrow(() ->
                        new RuntimeException("Wallet no existe"));

        if (wallet.getSaldo().compareTo(monto) < 0) {

                throw new SaldoInsuficienteException();
        }

        wallet.setSaldo(
                wallet.getSaldo().subtract(monto)
        );

        walletRepository.save(wallet);
        }

    @Override
    public BigDecimal obtenerSaldo(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return walletRepository.findByUsuario(usuario)
                .map(WalletCredito::getSaldo)
                .orElse(BigDecimal.ZERO);
    }
}