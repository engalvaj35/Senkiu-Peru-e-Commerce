package com.senkiu.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String guardarArchivo(MultipartFile archivo);
}