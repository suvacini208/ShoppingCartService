package com.suva.cart.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.suva.cart.domain.Cart;
import com.suva.cart.domain.CartDetails;
import com.suva.cart.domain.Item;
import com.suva.cart.domain.Order;
import com.suva.cart.service.CartService;

@RestController
@Validated
public class ShoppingCartController {

	@Autowired
	CartService cartService;

	/*
	 * Create cart in the datastore
	 */
	@PostMapping("/cart")
	public CartDetails createCart(@Valid @RequestBody Cart cart) {
		return cartService.createCart(cart);
	}

	@PostMapping("/cart/{cartId}/new/item")
	public CartDetails addItem(@PathVariable @Min(value = 1, message = "CartId cant be empty") long cartId,
			@Valid @RequestBody Item item) {
		return cartService.addItem(cartId, item);
	}

	@DeleteMapping("/cart/{cartId}/item/{itemId}")
	public CartDetails removeItem(@PathVariable @Min(value = 1, message = "CartId cant be empty") long cartId,
			@PathVariable @Min(value = 1, message = "ItemId cant be empty") long itemId) {
		return cartService.removeItem(cartId, itemId);
	}

	@PostMapping("/cart/{cartId}/item")
	public CartDetails updateItem(@PathVariable @Min(value = 1, message = "CartId cant be empty") long cartId,
			@Valid @RequestBody Item item) {
		return cartService.updateItem(cartId, item);
	}
	
	@GetMapping("/cart/{cartId}")
	public CartDetails getCartDetails(@PathVariable @Min(value = 1, message = "CartId cant be empty") long cartId) {
		return cartService.getCartInformation(cartId);
	}

	/*
	 * Checkout the shopping cart and create the order
	 */
	@PostMapping("/cart/{cartId}/checkout")
	public Order checkout(@PathVariable @Min(value = 1, message = "CartId cant be empty") long cartId) {
		return cartService.checkout(cartId);
	}

}
