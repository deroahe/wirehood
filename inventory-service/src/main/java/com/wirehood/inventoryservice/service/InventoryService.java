package com.wirehood.inventoryservice.service;

import com.wirehood.inventoryservice.dto.InventoryResponse;
import com.wirehood.inventoryservice.model.Inventory;
import com.wirehood.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        List<InventoryResponse> inventoryResponses = inventoryRepository.findBySkuCodeIn(skuCodes).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();
        Set<String> inStockSkuCodes = inventoryResponses.stream()
                .filter(InventoryResponse::isInStock)
                .map(InventoryResponse::getSkuCode)
                .collect(Collectors.toSet());
        Set<String> notInStockSkuCodes = new HashSet<>(skuCodes);
        notInStockSkuCodes.removeAll(inStockSkuCodes);

        log.info("Sku codes not in stock: {}", notInStockSkuCodes);

        return inventoryResponses;
    }

    public void save(Inventory inventory) {
        inventoryRepository.save(inventory);
        log.info("Inventory {} saved", inventory.getId());
    }
}
