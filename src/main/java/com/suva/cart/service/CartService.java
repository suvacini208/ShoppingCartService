package com.suva.cart.service;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.suva.cart.controller.CartException;
import com.suva.cart.domain.Address;
import com.suva.cart.domain.Cart;
import com.suva.cart.domain.CartDetails;
import com.suva.cart.domain.Order;
import com.suva.cart.domain.User;
import com.suva.cart.entity.DbSequence;
import com.suva.cart.entity.Item;
import com.suva.cart.repository.CartRepository;
import com.suva.cart.repository.DbSequenceRepository;

@Service
public class CartService {

	@Autowired
	DbSequenceRepository dbSeqRepo;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	RestTemplate restTemplate;

	@Value("${order.service.url}")
	private String orderServiceUrl;

	/**
	 * Create cart in the data store. Before persisting get the db sequence for cart
	 * 
	 * @param cart
	 * @return
	 */
	public CartDetails createCart(Cart cart) {

		com.suva.cart.entity.Cart cartEntity = copyCartToEntity(cart);
		cartEntity.setId(getSequence());
		return getCartDetails(cartRepository.save(cartEntity));

	}

	/**
	 * Generate the id sequence of cart and update the sequence table
	 * 
	 * @return id
	 */
	private long getSequence() {
		DbSequence dbSeq = dbSeqRepo.findById(Cart.DB_SEQ).orElse(new DbSequence());
		long seqId = dbSeq.getSeq() + 1;
		dbSeq.setSeq(seqId);
		dbSeq.setId(Cart.DB_SEQ);
		dbSeqRepo.save(dbSeq);

		return seqId;
	}

	/**
	 * Fetch the cart information from the data store and add the item in the cart
	 * and save
	 * 
	 * @param cartId
	 * @param item
	 * @return CartDetails
	 */
	public CartDetails addItem(long cartId, com.suva.cart.domain.Item item1) {
		com.suva.cart.entity.Cart cart = getCart(cartId);
		boolean itemAlreadyExist = false;

		for (Item item : cart.getItems()) {
			// If the item already present, then update the quantity
			if (item.getId() == item1.getId()) {
				item.setQuantity(item.getQuantity() + item1.getQuantity());
				itemAlreadyExist = true;
			}
		}

		// If the item not already present, then add the item in the cart
		if (!itemAlreadyExist) {
			Item newItem = new Item();
			BeanUtils.copyProperties(item1, newItem);
			cart.getItems().add(newItem);
		}
		return getCartDetails(cartRepository.save(cart));
	}

	/**
	 * Update the item in the cart to the repository
	 * 
	 * @param cartId
	 * @param item
	 * @return CardDetails
	 */
	public CartDetails updateItem(long cartId, com.suva.cart.domain.Item item1) {
		com.suva.cart.entity.Cart cart = getCart(cartId);

		for (Item item : cart.getItems()) {
			// If the item already present, then update the quantity
			if (item.getId() == item1.getId()) {
				item.setDescription(item1.getDescription());
				item.setPrice(item1.getPrice());
				item.setQuantity(item1.getQuantity());
			}
		}

		return getCartDetails(cartRepository.save(cart));
	}

	/**
	 * Remove the item of the corresponding item id from the cart
	 * 
	 * @param cartId
	 * @param itemId
	 * @return CardDetails
	 */
	public CartDetails removeItem(long cartId, long itemId) {
		com.suva.cart.entity.Cart cart = getCart(cartId);

		cart.setItems(cart.getItems().stream().filter(item -> item.getId() != itemId).collect(Collectors.toList()));
		return getCartDetails(cartRepository.save(cart));
	}

	/**
	 * Get the cart information of the respective cartId from repository
	 * 
	 * @param cartId
	 * @return cart entity
	 */
	private com.suva.cart.entity.Cart getCart(long cartId) {
		Optional<com.suva.cart.entity.Cart> cartOpt = cartRepository.findById(cartId);
		if (!cartOpt.isPresent()) {
			throw new CartException("Cart not present");
		}
		return cartOpt.get();
	}

	/**
	 * Copy Cart Entity to Cart Request
	 * 
	 * @param cart entity
	 * @return cart request
	 */
	private CartDetails getCartDetails(com.suva.cart.entity.Cart cart) {
		CartDetails newCartDetails = new CartDetails();
		BeanUtils.copyProperties(cart, newCartDetails);

		User newUser = new User();
		BeanUtils.copyProperties(cart.getUser(), newUser);

		newUser.setAddress(
				cart.getUser().getAddress().stream()
						.map(address -> new com.suva.cart.domain.Address(address.getAddressLine1(),
								address.getAddressLine2(), address.getCity(), address.getState(), address.getZip()))
						.collect(Collectors.toList()));
		newCartDetails.setUser(newUser);

		newCartDetails
				.setItems(cart
						.getItems().stream().map(item -> new com.suva.cart.domain.Item(item.getId(),
								item.getDescription(), item.getQuantity(), item.getPrice()))
						.collect(Collectors.toList()));
		return newCartDetails;
	}

	/**
	 * Copy Cart request to Cart Entity
	 * 
	 * @param cart request
	 * @return cart entity
	 */
	private com.suva.cart.entity.Cart copyCartToEntity(Cart cart) {
		com.suva.cart.entity.Cart newCartEntity = new com.suva.cart.entity.Cart();
		BeanUtils.copyProperties(cart, newCartEntity);

		com.suva.cart.entity.User newUserEntity = new com.suva.cart.entity.User();
		BeanUtils.copyProperties(cart.getUser(), newUserEntity);

		newUserEntity
				.setAddress(cart.getUser().getAddress().stream()
						.map(address -> new com.suva.cart.entity.Address(address.getAddressLine1(),
								address.getAddressLine2(), address.getCity(), address.getState(), address.getZip()))
						.collect(Collectors.toList()));
		newCartEntity.setUser(newUserEntity);

		return newCartEntity;
	}

	public Order checkout(@Min(value = 1, message = "CartId cant be empty") long cartId) {
		Optional<com.suva.cart.entity.Cart> cartOpt = cartRepository.findById(cartId);
		if (cartOpt.isPresent()) {
			com.suva.cart.entity.Cart cart = cartOpt.get();
			Order order = getOrder(cart);

			// Call Order service to initiate the order
			HttpEntity<Order> request = new HttpEntity<Order>(order);
			ResponseEntity<Order> orderEntity = restTemplate.postForEntity(orderServiceUrl, request, Order.class);
			if (orderEntity.getStatusCode() == HttpStatus.OK) {
				return orderEntity.getBody();
			}

			// Delete the shopping cart
			//TODO Not working - check
			cartRepository.deleteById(cartId);
		}
		return null;
	}

	private Order getOrder(com.suva.cart.entity.Cart cart) {
		Order order = new Order();
		order.setStatus("CREATED");

		User user = new User();
		BeanUtils.copyProperties(cart.getUser(), user);

		user.setAddress(cart
				.getUser().getAddress().stream().map(address -> new Address(address.getAddressLine1(),
						address.getAddressLine2(), address.getCity(), address.getState(), address.getZip()))
				.collect(Collectors.toList()));
		order.setUser(user);

		order.setItem(cart.getItems().stream().map(item -> new com.suva.cart.domain.Item(item.id, item.getDescription(),
				item.getQuantity(), item.getPrice())).collect(Collectors.toList()));

		return order;
	}

}
