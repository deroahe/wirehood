package com.wirehood.productservice.service;

import com.wirehood.productservice.client.InventoryClient;
import com.wirehood.productservice.dto.inventory.InventoryCreateDto;
import com.wirehood.productservice.dto.ProductCreateDto;
import com.wirehood.productservice.dto.ProductDto;
import com.wirehood.productservice.model.Product;
import com.wirehood.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static java.lang.String.format;
import static reactor.core.publisher.Mono.zip;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryClient inventoryClient;

    public Mono<String> createProduct(ProductCreateDto productCreateDto) {
        log.info("Creating product: <{}>", productCreateDto);

        var product = Product.builder()
                .name(productCreateDto.getName())
                .description(productCreateDto.getDescription())
                .price(productCreateDto.getPrice())
                .build();

        var inventoryCreateDto = InventoryCreateDto.builder()
                .skuCode(product.getName())
                .quantity(productCreateDto.getQuantity() != null
                        && productCreateDto.getQuantity() > 0
                        ? productCreateDto.getQuantity()
                        : 0)
                .build();

        var productMono = Mono.fromCallable(() -> productRepository.save(product))
                .doOnSuccess(p -> log.info("Product created: <{}>", p))
                .doOnError(t -> log.error("Error while saving product", t))
                .subscribeOn(Schedulers.boundedElastic());

        var inventoryMono = inventoryClient.createInventory(inventoryCreateDto)
                .doOnSuccess(i -> log.info("Inventory created: <{}>", i));

        return zip(productMono, inventoryMono, (p, i) -> format("Product saved: %s. Inventory saved: %s", p != null, i != null));
    }

    public Flux<ProductDto> getAllProducts() {
        log.info("Getting all products");
        var productListMono = Mono.fromCallable(productRepository::findAll)
                .doOnError(t -> log.error("Error while getting all products"))
                .subscribeOn(Schedulers.boundedElastic());

        return productListMono
                .flatMapIterable(products -> products.stream()
                        .map(this::convertProductToProductDto)
                        .toList());
    }

    private ProductDto convertProductToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
