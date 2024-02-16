package com.wirehood.apigateway.configuration;

import com.wirehood.apigateway.jwt.JwtService;
import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @SuppressWarnings("java:S116")
    private final Logger LOGGER = getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        final var request = exchange.getRequest();

        final var apiEndpoints = List.of("/register", "/login");
        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                LOGGER.warn("Denying incoming request to endpoint '{}' because of missing Authorization header", request.getURI());
                var response = exchange.getResponse();
                response.setStatusCode(UNAUTHORIZED);

                return response.setComplete();
            }

            final var token = request.getHeaders().getOrEmpty("authorization").get(0).substring(7);

            return jwtService.getClaims(token)
                    .map(claims -> {
                        LOGGER.info("Allowing incoming request to endpoint '{}' from '{}'", request.getURI(), claims.getSubject());
                        exchange.getRequest().mutate().header("id", String.valueOf(claims.get("id"))).build();

                        return chain.filter(exchange);
                    })
                    .getOrElseGet(ignoredThrowable -> {
                        LOGGER.warn("Denying incoming request to endpoint '{}' because of invalid JWT", request.getURI());
                        var response = exchange.getResponse();
                        response.setStatusCode(UNAUTHORIZED);

                        return response.setComplete();
                    });
        }
        LOGGER.info("Allowing incoming request to public endpoint '{}'", request.getURI());

        return chain.filter(exchange);
    }
}
