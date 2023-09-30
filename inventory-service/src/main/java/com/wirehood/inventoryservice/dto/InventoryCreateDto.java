package com.wirehood.inventoryservice.dto;

import lombok.*;

@Value
@Builder
public class InventoryCreateDto {

    String skuCode;
    Integer quantity;
}
