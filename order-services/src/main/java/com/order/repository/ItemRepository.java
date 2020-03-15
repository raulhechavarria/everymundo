package com.order.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.order.model.Item;
import com.order.model.Order;


@Repository
public interface ItemRepository extends MongoRepository<Item, Long> {

	
}