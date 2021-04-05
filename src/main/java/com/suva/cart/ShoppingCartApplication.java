package com.suva.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Shopping cart service is to provide operations to add, remove and update
 * items in the cart.
 *
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.suva.cart.repository")
public class ShoppingCartApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApplication.class, args);
	}
}
