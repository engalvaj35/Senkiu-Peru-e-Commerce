package com.senkiu.storage.service.impl;

import com.senkiu.storage.service.StorageService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class StorageServiceImpl
        implements StorageService {

    private static final String RUTA_UPLOAD =
            "src/main/resources/static/uploads/certificados/";

    @Override
    public String guardarArchivo(MultipartFile archivo) {

        try {

            String nombreOriginal =
                    archivo.getOriginalFilename();

            String extension =
                    nombreOriginal.substring(
                            nombreOriginal.lastIndexOf(".")
                    );

            String nombreArchivo =
                    UUID.randomUUID() + extension;

            Path ruta =
                    Paths.get(RUTA_UPLOAD + nombreArchivo);

            Files.createDirectories(ruta.getParent());

            Files.write(ruta, archivo.getBytes());

            return "/uploads/certificados/" + nombreArchivo;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Error guardando archivo"
            );
        }
    }
}