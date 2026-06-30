package com.tdis.documentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DocumentosServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DocumentosServiceApplication.class, args);
    }
}
