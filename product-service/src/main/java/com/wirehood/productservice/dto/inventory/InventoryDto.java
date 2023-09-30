package com.wirehood.productservice.dto.inventory;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@ToString
public class InventoryDto {

    String skuCode;
    boolean isInStock;
}
