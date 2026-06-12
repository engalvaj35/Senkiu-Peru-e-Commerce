package com.senkiu.util;

public class EstadoFormatter {

    public String formatear(String estado) {

        if (estado == null || estado.isBlank()) {
            return "";
        }

        String texto = estado
                .toLowerCase()
                .replace("_", " ");

        String[] palabras = texto.split(" ");

        StringBuilder resultado = new StringBuilder();

        for (String palabra : palabras) {

            resultado.append(
                    Character.toUpperCase(
                            palabra.charAt(0)
                    )
            );

            resultado.append(
                    palabra.substring(1)
            );

            resultado.append(" ");
        }

        return resultado.toString().trim();
    }

    public String formatearRol(String rol) {
        return switch (rol) {
            case "ROLE_USER" -> "Rol Usuario";
            case "ROLE_EMPRESA" -> "Rol Empresa";
            case "ROLE_ADMIN" -> "Rol Admin";
            case "ROLE_ONG" -> "Rol ONG";
            default -> rol;
        };
    }
}