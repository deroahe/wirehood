package com.wirehood.orderservice.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class OrderLineItemCreateDto {

    String skuCode;
    BigDecimal price;
    Integer quantity;
}
