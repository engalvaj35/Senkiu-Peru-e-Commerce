package com.senkiu.wallet.service;

import java.math.BigDecimal;

public interface WalletService {

    void crearWalletSiNoExiste(Long usuarioId);

    void sumarCreditos(Long usuarioId, BigDecimal monto);

    void restarCreditos(Long usuarioId, BigDecimal monto);

    BigDecimal obtenerSaldo(Long usuarioId);
}