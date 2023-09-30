package com.wirehood.productservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCreateDto {

    String name;
    String description;
    BigDecimal price;
    Integer quantity;
}
