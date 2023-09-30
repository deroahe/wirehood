package com.wirehood.orderservice.controller;

import com.wirehood.orderservice.dto.OrderCreateDto;
import com.wirehood.orderservice.dto.OrderDto;
import com.wirehood.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> placeOrder(@RequestBody OrderCreateDto orderCreateDto) {
        return orderService.placeOrder(orderCreateDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }
}
