package com.wirehood.productservice.config;

import com.wirehood.inventoryservice.api.InventoryApi;
import com.wirehood.inventoryservice.sdk.InventoryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public InventoryApi inventoryApi(WebClient.Builder builder) {
        return new InventoryClient(builder);
    }
}
