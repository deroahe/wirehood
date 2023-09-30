package com.wirehood.inventoryservice.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InventoryStockDto {

    String skuCode;
    Boolean isInStock;
}
