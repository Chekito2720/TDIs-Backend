package com.tdis.tramites.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "documentos-service")
public interface DocumentosClient {

    @PostMapping(value = "/api/documentos/upload/{solicitudId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String, String> subirArchivo(@PathVariable("solicitudId") UUID solicitudId,
                                     @RequestPart("archivo") MultipartFile archivo);

    @GetMapping(value = "/api/documentos/download/{solicitudId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    byte[] descargarArchivoBytes(@PathVariable("solicitudId") UUID solicitudId);

    @DeleteMapping("/api/documentos/{solicitudId}")
    void eliminarArchivo(@PathVariable("solicitudId") UUID solicitudId);
}
