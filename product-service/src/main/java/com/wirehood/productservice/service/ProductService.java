package com.wirehood.productservice.service;

import com.wirehood.productservice.dto.InventoryRequest;
import com.wirehood.productservice.dto.ProductRequest;
import com.wirehood.productservice.dto.ProductResponse;
import com.wirehood.productservice.model.Product;
import com.wirehood.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final WebClient.Builder webClientBuilder;

    public String createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} saved.", product.getId());

        InventoryRequest inventoryRequest = InventoryRequest.builder()
                .skuCode(product.getName())
                .quantity(productRequest.getQuantity() != null
                        && productRequest.getQuantity() > 0
                        ? productRequest.getQuantity()
                        : 0)
                .build();

        // call inventory to update stock

        return webClientBuilder.build().post()
                .uri("http://inventory-service/api/inventory")
                .body(Mono.just(inventoryRequest), InventoryRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
