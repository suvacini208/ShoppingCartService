package com.suva.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Shopping cart service is to provide operations to add, remove and update
 * items in the cart.
 *
 */
@SpringBootApplication
public class ShoppingCartApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApplication.class, args);
	}
}
