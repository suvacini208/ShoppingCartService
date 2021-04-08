package com.suva.cart.service;

import static com.suva.cart.utils.TestUtils.getCartDetails;
import static com.suva.cart.utils.TestUtils.getCartDetailsWithItem;
import static com.suva.cart.utils.TestUtils.getCartDetailsWithUptItem;
import static com.suva.cart.utils.TestUtils.getCartDomain;
import static com.suva.cart.utils.TestUtils.getOrder;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.suva.cart.controller.CartException;
import com.suva.cart.domain.Cart;
import com.suva.cart.domain.CartDetails;
import com.suva.cart.domain.Item;
import com.suva.cart.domain.Order;
import com.suva.cart.entity.DbSequence;
import com.suva.cart.repository.CartRepository;
import com.suva.cart.repository.DbSequenceRepository;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {

	@Mock
	CartRepository cartRepository;

	@Mock
	DbSequenceRepository dbSeqRepo;

	@Mock
	RestTemplate restTemplate;

	@InjectMocks
	CartService cartService;
	
	@Test
	public void testAddCartSuccess() {
		Cart cart = getCartDomain();
		DbSequence dbSeq = new DbSequence();
		dbSeq.setSeq(1);
		dbSeq.setId(Cart.DB_SEQ);
		Optional<com.suva.cart.entity.Cart> cartEntityOpt = getCartDetails();
		com.suva.cart.entity.Cart cartEntity = cartEntityOpt.get();
		cartEntity.setId(1);
		List<com.suva.cart.entity.Item> itemList = new ArrayList<com.suva.cart.entity.Item>();
		cartEntity.setItems(itemList);
		when(cartRepository.save(cartEntity)).thenReturn(cartEntity);
		CartDetails cartDetails = cartService.createCart(cart);
		assert(cartDetails.getId()==1);
	}

	@Test
	public void testAddItemSuccess() {
		Item item1 = new Item(1, "Beats Headphones", 5, 300);
		Long cartId = Long.valueOf(10);
		when(cartRepository.findById(cartId)).thenReturn(getCartDetails());
		when(cartRepository.save(getCartDetailsWithItem())).thenReturn(getCartDetailsWithItem());
		CartDetails cartDetails = cartService.addItem(cartId, item1);
		assert (cartDetails.getItems().size() == 1);
		assertArrayEquals(cartDetails.getItems().stream().toArray(), new Item[] { item1 });
	}

	@Test
	public void testAddExistingItemSuccess() {
		Item item1 = new Item(1, "Beats Headphones", 2, 300);
		Long cartId = Long.valueOf(10);
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(getCartDetailsWithItem()));
		when(cartRepository.save(getCartDetailsWithUptItem())).thenReturn(getCartDetailsWithUptItem());
		CartDetails cartDetails = cartService.addItem(cartId, item1);
		assert (cartDetails.getItems().size() == 1);
		assertArrayEquals(cartDetails.getItems().stream().toArray(),
				new Item[] { new Item(1, "Beats Headphones", 7, 300) });
	}
	
	@Test
	public void testAddItemNoCart() {
		Item item1 = new Item(1, "Beats Headphones", 2, 300);
		Long cartId = Long.valueOf(10);
		when(cartRepository.findById(cartId)).thenThrow(new CartException("Cart not present"));
		Assertions.assertThrows(CartException.class, () -> cartService.addItem(cartId, item1));
	}
	
	@Test
	public void testUpdateItemSuccess() {
		Item item1 = new Item(1, "Beats Headphones", 5, 300);
		Long cartId = Long.valueOf(10);
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(getCartDetailsWithUptItem()));
		when(cartRepository.save(getCartDetailsWithItem())).thenReturn(getCartDetailsWithItem());
		CartDetails cartDetails = cartService.updateItem(cartId, item1);
		assert (cartDetails.getItems().size() == 1);
		assertArrayEquals(cartDetails.getItems().stream().toArray(),
				new Item[] { new Item(1, "Beats Headphones", 5, 300) });
	}
	
	@Test
	public void testUpdateItemNoCart() {
		Item item1 = new Item(1, "Beats Headphones", 2, 300);
		Long cartId = Long.valueOf(10);
		when(cartRepository.findById(cartId)).thenThrow(new CartException("Cart not present"));
		Assertions.assertThrows(CartException.class, () -> cartService.updateItem(cartId, item1));
	}

	@Test
	@Ignore 
	public void testCheckout() {
		Long cartId = Long.valueOf(10);
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(getCartDetailsWithItem()));
		Order orderWithNumber = getOrder();
		orderWithNumber.setId(1);
		
		// Not mocked properly.  Troubleshoot
		when(restTemplate.postForEntity(ArgumentMatchers.anyString(),  ArgumentMatchers.any(), ArgumentMatchers.<Class<Order>>any())).thenReturn(new ResponseEntity<Order>(orderWithNumber, HttpStatus.OK) );
		Order order = cartService.checkout(cartId);
		assert(order.getId()==1);
	}

}
