package com.wirehood.orderservice.service;

import com.wirehood.orderservice.dto.InventoryResponse;
import com.wirehood.orderservice.dto.OrderLineItemDto;
import com.wirehood.orderservice.dto.OrderRequest;
import com.wirehood.orderservice.dto.OrderResponse;
import com.wirehood.orderservice.model.Order;
import com.wirehood.orderservice.model.OrderLineItem;
import com.wirehood.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItemList = orderRequest.getOrderLineItemDtoList()
                .stream()
                .map(this::dtoToOrderLineItem)
                .toList();

        order.setOrderLineItemsList(orderLineItemList);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItem::getSkuCode)
                .toList();

        // call inventory service and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
            log.info("Order {} saved", order.getId());
        } else {
            throw new IllegalArgumentException("One or more products are not in stock, try again later");
        }
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(this::orderToDto).toList();
    }

    private OrderLineItem dtoToOrderLineItem(OrderLineItemDto orderLineItemDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemDto.getPrice());
        orderLineItem.setQuantity(orderLineItemDto.getQuantity());
        orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());

        return orderLineItem;
    }

    private OrderResponse orderToDto(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderLineItemsList(order.getOrderLineItemsList().stream()
                        .map(this::orderLineItemToDto).toList())
                .build();
    }

    private OrderLineItemDto orderLineItemToDto(OrderLineItem orderLineItem) {
        return OrderLineItemDto.builder()
                .id(orderLineItem.getId())
                .price(orderLineItem.getPrice())
                .skuCode(orderLineItem.getSkuCode())
                .quantity(orderLineItem.getQuantity())
                .build();
    }
}
