package com.senkiu.wallet.exception;

public class SaldoInsuficienteException extends RuntimeException {

    public SaldoInsuficienteException() {
        super("No tienes créditos suficientes para realizar esta compra.");
    }
}