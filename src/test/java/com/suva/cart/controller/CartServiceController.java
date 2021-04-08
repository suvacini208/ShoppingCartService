package com.suva.cart.controller;

import static com.suva.cart.utils.TestUtils.getCardDetailsD;
import static com.suva.cart.utils.TestUtils.getCartDomain;
import static com.suva.cart.utils.TestUtils.getOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suva.cart.domain.Cart;
import com.suva.cart.domain.CartDetails;
import com.suva.cart.domain.Item;
import com.suva.cart.domain.Order;
import com.suva.cart.service.CartService;

@WebMvcTest
@RunWith(SpringRunner.class)
public class CartServiceController {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CartService cartService;

	private static ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testAddItem() throws Exception {
		long cartId = 10;
		Item item1 = new Item(1, "Beats Headphones", 5, 300);
		String json = mapper.writeValueAsString(item1);
		Mockito.when(cartService.addItem(cartId, item1)).thenReturn(getCardDetailsD());
		mockMvc.perform(post("/cart/10/new/item").content(json).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.items[0].id", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.items[0].quantity", Matchers.equalTo(5)))
				.andExpect(jsonPath("$.items[0].price", Matchers.equalTo(300.0)))
				.andExpect(jsonPath("$.items[0].description", Matchers.equalTo("Beats Headphones")));
	}
	
	@Test
	public void testAddItemBadInput() throws Exception {
		long cartId = 10;
		Item item1 = new Item(1, "Beats Headphones", 5, 300);
		item1.setQuantity(0);
		String json = mapper.writeValueAsString(item1);
		Mockito.when(cartService.addItem(cartId, item1)).thenReturn(getCardDetailsD());
		mockMvc.perform(post("/cart/10/new/item").content(json).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.[0].message", Matchers.equalTo("Quantity can't be less than 1")));
	}
	
	@Test
	public void testUpdateItem() throws Exception {
		long cartId = 10;
		Item item1 = new Item(1, "Beats Headphones", 5, 300);
		String json = mapper.writeValueAsString(item1);
		Mockito.when( cartService.updateItem(cartId, item1)).thenReturn(getCardDetailsD());
		mockMvc.perform(post("/cart/10/item").content(json).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.items[0].id", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.items[0].quantity", Matchers.equalTo(5)))
				.andExpect(jsonPath("$.items[0].price", Matchers.equalTo(300.0)))
				.andExpect(jsonPath("$.items[0].description", Matchers.equalTo("Beats Headphones")));
	}
	
	@Test
	public void testUpdateItemBadInput() throws Exception {
		long cartId = 10;
		Item item1 = new Item(0, "Beats Headphones", 5, 300);
		String json = mapper.writeValueAsString(item1);
		Mockito.when(cartService.addItem(cartId, item1)).thenReturn(getCardDetailsD());
		mockMvc.perform(post("/cart/10/item").content(json).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.[0].message", Matchers.equalTo("Id can't be less than 1")));
	}
	
	@Test
	public void testRemoveItem() throws Exception {
		long cartId = 10;
		CartDetails cartDetails = getCardDetailsD();
		cartDetails.setItems(null);
		Mockito.when( cartService.removeItem(cartId, 1)).thenReturn(cartDetails);
		mockMvc.perform(delete("/cart/10/item/1").accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.items").isEmpty());
	}
	
	@Test
	public void addCart() throws Exception {
		Cart cart = getCartDomain();
		cart.setId(0); 
		CartDetails cartDetailsNew=getCardDetailsD(); 
		cartDetailsNew.setId(10); 
		cartDetailsNew.setItems(null);
		String json = mapper.writeValueAsString(cart);
		Mockito.when(cartService.createCart(ArgumentMatchers.any())).thenReturn(cartDetailsNew);
		mockMvc.perform(post("/cart").content(json).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", Matchers.equalTo(10)));
	}
	
	@Test
	public void checkout() throws Exception {
		Order order = getOrder();
		order.setId(100);
		Mockito.when(cartService.checkout(10)).thenReturn(order );
		mockMvc.perform(post("/cart/10/checkout").accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", Matchers.equalTo(100)));
	}

}
