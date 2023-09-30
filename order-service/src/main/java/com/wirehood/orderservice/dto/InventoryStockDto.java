package com.wirehood.orderservice.dto;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@ToString
public class InventoryStockDto {

    String skuCode;
    Boolean isInStock;
}
