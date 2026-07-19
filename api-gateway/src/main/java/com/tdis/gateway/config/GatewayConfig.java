package com.tdis.gateway.config;

import com.tdis.gateway.security.JwtAuthFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public GatewayConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth-open", r -> r
                .path("/api/auth/login")
                .filters(f -> f
                    .removeRequestHeader("Authorization"))
                .uri("http://localhost:8081"))
            .route("auth-register-open", r -> r
                .path("/api/auth/register")
                .filters(f -> f
                    .removeRequestHeader("Authorization"))
                .uri("http://localhost:8081"))
            .route("auth-register-externo-open", r -> r
                .path("/api/auth/register-externo")
                .filters(f -> f
                    .removeRequestHeader("Authorization"))
                .uri("http://localhost:8081"))
            .route("usuarios-service", r -> r
                .path("/api/auth/**", "/api/usuarios/**", "/api/admin/**")
                .filters(f -> f
                    .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                .uri("http://localhost:8081"))
            .route("catalogo-service", r -> r
                .path("/api/catalogo/**")
                .filters(f -> f
                    .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                .uri("http://localhost:8082"))
            .route("tramites-service", r -> r
                .path("/api/solicitudes/**")
                .filters(f -> f
                    .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                .uri("http://localhost:8083"))
            .route("progreso-service", r -> r
                .path("/api/progreso/**")
                .filters(f -> f
                    .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                .uri("http://localhost:8084"))
            .route("documentos-service", r -> r
                .path("/api/documentos/**")
                .filters(f -> f
                    .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                .uri("http://localhost:8085"))
            .build();
    }
}
