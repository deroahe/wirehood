package com.wirehood.inventoryservice.sdk;

import com.wirehood.inventoryservice.api.InventoryApi;
import com.wirehood.inventoryservice.dto.InventoryCreateDto;
import com.wirehood.inventoryservice.dto.InventoryDto;
import com.wirehood.inventoryservice.dto.InventoryStockDto;
import org.slf4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class InventoryClient implements InventoryApi {

    private final String HTTP_FORMAT = "http://%s";
    private final String INVENTORY_SERVICE = "inventory-service";

    private final Logger LOGGER = getLogger(InventoryClient.class);

    private final WebClient webClient;

    public InventoryClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl(format(HTTP_FORMAT, INVENTORY_SERVICE))
                .filter(((request, next) -> {
                    LOGGER.info("Sending request with URL {}", request.url());
                    return next.exchange(request);
                }))
                .build();
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
                .accept(APPLICATION_JSON)
                .bodyValue(inventoryCreateDto)
                .retrieve()
                .bodyToMono(InventoryDto.class);
    }
}
