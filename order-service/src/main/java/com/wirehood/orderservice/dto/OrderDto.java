package com.wirehood.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private String orderNumber;
    private List<OrderLineItemDto> orderLineItemsList;
}
