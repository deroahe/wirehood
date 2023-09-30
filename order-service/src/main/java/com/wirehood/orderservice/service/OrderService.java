package com.wirehood.orderservice.service;

import com.wirehood.orderservice.client.InventoryClient;
import com.wirehood.orderservice.dto.*;
import com.wirehood.orderservice.model.Order;
import com.wirehood.orderservice.model.OrderLineItem;
import com.wirehood.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

import static java.lang.Boolean.FALSE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public Mono<String> placeOrder(OrderCreateDto orderCreateDto) {
        log.info("Placing order {}", orderCreateDto);

        var orderLineItemList = orderCreateDto.getOrderLineItemCreateDtoList().stream()
                .map(this::convertOrderLineItemCreateDtoToOrderLineItem)
                .toList();

        var skuCodes = orderLineItemList.stream()
                .map(OrderLineItem::getSkuCode)
                .toList();

        var inventoryFlux = inventoryClient.checkStockMultiple(skuCodes);

        var allProductsInStockMono = inventoryFlux
                .map(InventoryStockDto::getIsInStock)
                .switchIfEmpty(Mono.just(FALSE))
                .reduce(true, (x1, x2) -> x1 && x2);

        return allProductsInStockMono.flatMap(inStock -> {
                    if (inStock) {
                        var order = Order.builder()
                                .orderNumber(UUID.randomUUID().toString())
                                .orderLineItemsList(orderLineItemList)
                                .build();
                        return Mono.fromCallable(() -> orderRepository.save(order))
                                .subscribeOn(Schedulers.boundedElastic())
                                .doOnSuccess(o -> {
                                    log.info("Order {} created successfully", o);
                                })
                                .then(Mono.just("All items are in stock. Order placed successfully"));

                    } else {
                        log.info("Not all items are in stock. Order not created successfully");
                        return Mono.just("Not all the items are in stock. Order not placed successfully");
                    }
                });
    }

    public Flux<OrderDto> getAllOrders() {
        log.info("Getting all orders");

        var orderListMono = Mono.fromCallable(orderRepository::findAll)
                .subscribeOn(Schedulers.boundedElastic());

        return orderListMono
                .flatMapIterable(orders -> orders.stream()
                        .map(this::convertOrderToOrderDto)
                        .toList());
    }

    private OrderLineItem convertOrderLineItemCreateDtoToOrderLineItem(OrderLineItemCreateDto orderLineItemCreateDto) {
        return OrderLineItem.builder()
                .price(orderLineItemCreateDto.getPrice())
                .quantity(orderLineItemCreateDto.getQuantity())
                .skuCode(orderLineItemCreateDto.getSkuCode())
                .build();
    }

    private OrderDto convertOrderToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderLineItemsList(order.getOrderLineItemsList().stream()
                        .map(this::orderLineItemToOrderLineItemDto)
                        .toList())
                .build();
    }

    private OrderLineItemDto orderLineItemToOrderLineItemDto(OrderLineItem orderLineItem) {
        return OrderLineItemDto.builder()
                .id(orderLineItem.getId())
                .price(orderLineItem.getPrice())
                .skuCode(orderLineItem.getSkuCode())
                .quantity(orderLineItem.getQuantity())
                .build();
    }
}
