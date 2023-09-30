package com.wirehood.productservice.controller;

import com.wirehood.productservice.dto.ProductCreateDto;
import com.wirehood.productservice.dto.ProductDto;
import com.wirehood.productservice.service.ProductService;
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
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> createProduct(@RequestBody ProductCreateDto productCreateDto) {
        return productService.createProduct(productCreateDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
