package com.wirehood.orderservice.client;

import com.wirehood.orderservice.dto.InventoryStockDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.lang.String.format;

@Component
@Slf4j
public class InventoryClient {

    private final String HTTP_FORMAT = "http://%s";
    private final String IN_STOCK_SINGLE_URI = "/api/inventory/";
    private final String IN_STOCK_MULTIPLE_URI = "/api/inventory/are-in-stock";

    private final WebClient inventoryClient;

    public InventoryClient(@Value("${client.inventory.host:inventory-service}")
                           String inventoryHost,
                           WebClient.Builder webClientBuilder) {
        this.inventoryClient = webClientBuilder
                .baseUrl(format(HTTP_FORMAT, inventoryHost))
                .build();
    }

    public Mono<Boolean> checkStockSingle(String skuCode) {
        return inventoryClient.get()
                .uri(IN_STOCK_SINGLE_URI,
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCode).build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(t -> log.info("Error while checking stock for single skuCode {}", skuCode, t));
    }

    public Flux<InventoryStockDto> checkStockMultiple(List<String> skuCodes) {
        return inventoryClient.get()
                .uri(IN_STOCK_MULTIPLE_URI,
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToFlux(InventoryStockDto.class)
                .doOnError(t -> log.info("Error while checking stock for multiple skuCodes {}", skuCodes, t));
    }
}
