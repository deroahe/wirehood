package com.wirehood.inventoryservice.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class InventoryDto {

    String skuCode;
    Integer quantity;
}
