package com.wirehood.inventoryservice.api;

import com.wirehood.inventoryservice.dto.InventoryCreateDto;
import com.wirehood.inventoryservice.dto.InventoryDto;
import com.wirehood.inventoryservice.dto.InventoryStockDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface InventoryApi {

    String INVENTORY_PATH = "/api/inventory";

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> isInStock(@RequestParam String skuCode);

    @GetMapping("/are-in-stock")
    @ResponseStatus(HttpStatus.OK)
    public Flux<InventoryStockDto> areInStock(@RequestParam List<String> skuCode);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<InventoryDto> addStock(@RequestBody InventoryCreateDto inventoryCreateDto);
}
