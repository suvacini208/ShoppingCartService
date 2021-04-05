package com.suva.cart.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.suva.cart.entity.DbSequence;

public interface DbSequenceRepository extends MongoRepository<DbSequence, String>{

}
