package com.wirehood.inventoryservice.dto;

import lombok.*;

@Value
@Builder
public class InventoryDto {

    String skuCode;
    Integer quantity;
}
