package com.wirehood.apigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final JwtAuthenticationFilter filter;

    public GatewayConfig(JwtAuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("order-service", r -> r.path("/api/order/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://order-service"))
                .route("inventory-service", r -> r.path("/api/inventory/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://inventory-service"))
                .route("product-service", r -> r.path("/api/product/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://product-service"))
                .build();
    }
}
