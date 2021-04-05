package com.suva.cart.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.suva.cart.entity.Cart;

public interface CartRepository extends MongoRepository<Cart, Long> {

}
