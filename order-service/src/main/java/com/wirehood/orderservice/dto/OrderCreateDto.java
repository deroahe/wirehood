package com.wirehood.orderservice.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class OrderCreateDto {

    List<OrderLineItemCreateDto> orderLineItemCreateDtoList;
}
