package com.wirehood.productservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
public class ProductDto {

    String id;
    String name;
    String description;
    BigDecimal price;
}
