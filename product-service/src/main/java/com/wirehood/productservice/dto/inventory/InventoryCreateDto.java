package com.wirehood.productservice.dto.inventory;

import lombok.*;

@Value
@Builder
public class InventoryCreateDto {

    String skuCode;
    Integer quantity;
}
