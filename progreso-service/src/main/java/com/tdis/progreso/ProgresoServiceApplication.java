package com.tdis.progreso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ProgresoServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProgresoServiceApplication.class, args);
    }
}
