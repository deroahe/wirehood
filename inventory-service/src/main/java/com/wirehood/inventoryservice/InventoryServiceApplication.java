package com.wirehood.inventoryservice;

import com.wirehood.inventoryservice.model.Inventory;
import com.wirehood.inventoryservice.service.InventoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
@EnableEurekaClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

//	Initial load for DB
//	@Bean
//	public CommandLineRunner loadData(InventoryService inventoryService) {
//		return args -> {
//			Inventory inventory1 = new Inventory();
//			inventory1.setSkuCode("tv-" + UUID.randomUUID());
//			inventory1.setQuantity(100);
//
//			Inventory inventory2 = new Inventory();
//			inventory2.setSkuCode("car-" + UUID.randomUUID());
//			inventory2.setQuantity(0);
//
//
//			inventoryService.save(inventory1);
//			inventoryService.save(inventory2);
//		};
//	}
}
