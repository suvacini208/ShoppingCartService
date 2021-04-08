package com.suva.cart.utils;
import java.util.Arrays;
import java.util.Optional;

import com.suva.cart.domain.Address;
import com.suva.cart.domain.Cart;
import com.suva.cart.domain.CartDetails;
import com.suva.cart.domain.Item;
import com.suva.cart.domain.Order;
import com.suva.cart.entity.User;

public class TestUtils {
	
	
	public static Order getOrder() {
		return new Order("CREATED", null, Arrays.asList(new Item(1, "Beats Headphones", 7, 300)), new com.suva.cart.domain.User());
	}

	public static Optional<com.suva.cart.entity.Cart> getCartDetails() {
		return Optional.of(new com.suva.cart.entity.Cart(10, null, new User("Sanpreet", "test@test.com", "4532124543",
				Arrays.asList(new com.suva.cart.entity.Address("9 Tonsi st", "", "Toronto", "ON", "L7A0T9")))));
	}

	public static com.suva.cart.entity.Cart getCartDetailsWithItem() {
		return new com.suva.cart.entity.Cart(10,
				Arrays.asList(new com.suva.cart.entity.Item(1, "Beats Headphones", 5, 300)),
				new User("Sanpreet", "test@test.com", "4532124543",
						Arrays.asList(new com.suva.cart.entity.Address("9 Tonsi st", "", "Toronto", "ON", "L7A0T9"))));
	}

	public static com.suva.cart.entity.Cart getCartDetailsWithUptItem() {
		return new com.suva.cart.entity.Cart(10,
				Arrays.asList(new com.suva.cart.entity.Item(1, "Beats Headphones", 7, 300)),
				new User("Sanpreet", "test@test.com", "4532124543",
						Arrays.asList(new com.suva.cart.entity.Address("9 Tonsi st", "", "Toronto", "ON", "L7A0T9"))));
	}
	
	public static Cart getCartDomain() {
		return new Cart(new com.suva.cart.domain.User("Sanpreet", "test@test.com", "4532124543",
						Arrays.asList(new Address("9 Tonsi st", "", "Toronto", "ON", "L7A0T9"))));
	}
	

	public static CartDetails getCardDetailsD() {
		return new CartDetails(10,
				Arrays.asList(new Item(1, "Beats Headphones", 5, 300)),
				new com.suva.cart.domain.User("Sanpreet", "test@test.com", "4532124543",
						Arrays.asList(new Address("9 Tonsi st", "", "Toronto", "ON", "L7A0T9"))));
	}
	
}
