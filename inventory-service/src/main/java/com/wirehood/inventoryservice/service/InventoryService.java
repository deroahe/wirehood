package com.wirehood.inventoryservice.service;

import com.wirehood.inventoryservice.dto.InventoryCreateDto;
import com.wirehood.inventoryservice.dto.InventoryDto;
import com.wirehood.inventoryservice.dto.InventoryStockDto;
import com.wirehood.inventoryservice.model.Inventory;
import com.wirehood.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Flux.fromStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public Flux<InventoryStockDto> areInStock(List<String> skuCodes) {
        log.info("Checking stock for skuCodes: <{}>", skuCodes);

        var logString = new StringBuilder("In stock: ");
        var inventoryListMono = Mono.fromCallable(() -> inventoryRepository.findBySkuCodeIn(skuCodes))
                .doOnError(t -> log.error("Error while getting stock for multiple skus", t))
                .subscribeOn(Schedulers.boundedElastic());

        return fromStream(() -> skuCodes.stream()
                .flatMap(skuCode -> inventoryListMono
                        .flatMapMany(inventoryList -> fromIterable(inventoryList)
                                .filter(inventory -> StringUtils.equals(skuCode, inventory.getSkuCode()))
                                .switchIfEmpty(Mono.just(Inventory.builder()
                                        .skuCode(skuCode)
                                        .quantity(0)
                                        .build()))
                                .map(inventory -> InventoryStockDto.builder()
                                        .skuCode(inventory.getSkuCode())
                                        .isInStock(inventory.getQuantity() > 0)
                                        .build())
                                .doOnNext(i -> logString.append(format("<%s:%s> ", skuCode, i.getIsInStock())))
                        ).toStream()))
                .doOnComplete(() -> log.info(String.valueOf(logString)));
    }

    public Mono<Boolean> isInStock(String skuCode) {
        log.info("Checking stock for skuCode: <{}>", skuCode);

        var inventoryMono = Mono.fromCallable(() -> ofNullable(inventoryRepository.findBySkuCode(skuCode)))
                .doOnError(t -> log.error("Error while checking stock for single sku", t))
                .subscribeOn(Schedulers.boundedElastic());

        return inventoryMono
                .map(inventoryOptional -> inventoryOptional
                        .map(inventory -> inventory.getQuantity() > 0)
                        .orElse(FALSE))
                .doOnSuccess(inStock -> log.info("In stock: <{}:{}>", skuCode, inStock));
    }

    public Mono<InventoryDto> addStock(InventoryCreateDto inventoryCreateDto) {
        log.info("Saving inventory: <{}>", inventoryCreateDto);

        var inventory = Inventory.builder()
                .skuCode(inventoryCreateDto.getSkuCode())
                .quantity(inventoryCreateDto.getQuantity())
                .build();

        var inventoryMono = save(inventory);

        return inventoryMono.map(this::convertInventoryToInventoryDto);
    }

    private Mono<Inventory> save(Inventory inventory) {
        return Mono.fromCallable(() -> inventoryRepository.save(inventory))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(i -> log.info("Inventory saved successfully: <{}>", i))
                .doOnError(t -> log.error("Error while saving inventory", t));
    }

    private InventoryDto convertInventoryToInventoryDto(Inventory inventory) {
        return InventoryDto.builder()
                .skuCode(inventory.getSkuCode())
                .quantity(inventory.getQuantity())
                .build();
    }
}
