package com.wirehood.inventoryservice.controller;

import com.wirehood.inventoryservice.dto.InventoryCreateDto;
import com.wirehood.inventoryservice.dto.InventoryDto;
import com.wirehood.inventoryservice.model.Inventory;
import com.wirehood.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> isInStock(@RequestParam String skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @GetMapping("/are-in-stock")
    @ResponseStatus(HttpStatus.OK)
    public Flux<InventoryDto> areInStock(@RequestParam List<String> skuCode) {
        return inventoryService.areInStock(skuCode);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Inventory> addStock(@RequestBody InventoryCreateDto inventoryCreateDto) {
        return inventoryService.save(inventoryCreateDto);
    }
}
