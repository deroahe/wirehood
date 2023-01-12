package com.wirehood.apigateway.configuration;

import com.wirehood.apigateway.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
//                .route("authentication-service", r -> r.path("/api/authentication")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://authentication-service"))
                .route("order-service", r -> r.path("/api/order/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://order-service"))
                .route("echo", r -> r.path("/echo/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://echo"))
                .route("hello", r -> r.path("/hello/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://hello"))
                .build();
    }
}
