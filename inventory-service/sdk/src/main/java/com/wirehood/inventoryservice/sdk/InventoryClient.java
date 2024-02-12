package com.wirehood.inventoryservice.sdk;

import com.wirehood.inventoryservice.api.InventoryApi;
import com.wirehood.inventoryservice.dto.InventoryCreateDto;
import com.wirehood.inventoryservice.dto.InventoryDto;
import com.wirehood.inventoryservice.dto.InventoryStockDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class InventoryClient implements InventoryApi {

    private final String HTTP_FORMAT = "http://%s";

    private final WebClient webClient;

    public InventoryClient(@Value("${client.inventory.host:inventory-service}")
                           String inventoryHost,
                           WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(format(HTTP_FORMAT, inventoryHost))
                .build();
    }

    // todo: test and remove
    public Mono<InventoryDto> createInventoryNew(InventoryCreateDto inventoryCreateDto) {
        return webClient.post()
                .uri(INVENTORY_PATH)
                .accept(APPLICATION_JSON)
                .bodyValue(inventoryCreateDto)
                .retrieve()
                .bodyToMono(InventoryDto.class);
    }

    @Override
    public Mono<Boolean> isInStock(String skuCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(INVENTORY_PATH)
                        .queryParam("skuCode", skuCode)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Boolean.class);

    }

    @Override
    public Flux<InventoryStockDto> areInStock(List<String> skuCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(INVENTORY_PATH + "/are-in-stock")
                        .queryParam("skuCode", skuCode)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(InventoryStockDto.class);
    }

    @Override
    public Mono<InventoryDto> addStock(InventoryCreateDto inventoryCreateDto) {
        return webClient.post()
                .uri(INVENTORY_PATH)
                .bodyValue(inventoryCreateDto)
                .retrieve()
                .bodyToMono(InventoryDto.class);
    }
}
