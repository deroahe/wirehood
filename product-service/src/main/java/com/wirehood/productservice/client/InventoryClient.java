package com.wirehood.productservice.client;

import com.wirehood.productservice.dto.inventory.InventoryCreateDto;
import com.wirehood.productservice.dto.inventory.InventoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Component
@Slf4j
public class InventoryClient {

    private final String HTTP_FORMAT = "http://%s";
    private final String INVENTORY_URI = "/api/inventory";

    private final WebClient inventoryClient;

    public InventoryClient(@Value("${client.inventory.host:inventory-service}")
                           String inventoryHost,
                           WebClient.Builder webClientBuilder) {
        this.inventoryClient = webClientBuilder
                .baseUrl(format(HTTP_FORMAT, inventoryHost))
                .build();
    }

    public Mono<InventoryDto> createInventory(InventoryCreateDto inventoryCreateDto) {
        return inventoryClient.post()
                .uri(INVENTORY_URI)
                .body(Mono.just(inventoryCreateDto), InventoryCreateDto.class)
                .retrieve()
                .bodyToMono(InventoryDto.class)
                .doOnError(t -> log.error("Error calling inventory-service", t))
                .doOnSuccess(System.out::println);
    }
}
