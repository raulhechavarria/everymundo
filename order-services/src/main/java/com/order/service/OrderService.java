package com.order.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.order.exception.ResourceNotFoundException;
import com.order.model.Order;


public interface OrderService {

	void saveItems(Order order);

	void updateItems(@Valid Order orderDetails, Order order);

	void delByCustomerId(Long customerId);

	List<String> getOrderList(Long customerId);

	void delByCustomerId();

	
	

}
