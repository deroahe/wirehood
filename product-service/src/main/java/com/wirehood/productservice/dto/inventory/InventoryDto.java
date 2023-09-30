package com.wirehood.productservice.dto.inventory;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class InventoryDto {

    String skuCode;
    boolean isInStock;
}
