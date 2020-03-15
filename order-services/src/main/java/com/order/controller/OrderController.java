package com.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.ls.LSInput;

import com.order.exception.ResourceNotFoundException;
import com.order.model.Order;
import com.order.repository.OrderRepository;
import com.order.service.OrderService;
import com.order.service.impl.SequenceGeneratorService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	@Autowired
	private OrderRepository repository;

	@Autowired
	OrderService service;

	@GetMapping
	@Cacheable(value = "id", key = "customerid")
	public List<Order> getAllOrder() {
		return repository.findAll();
	}

	@GetMapping("{id}")
	@Cacheable(value = "id", key = "customerid")
	public ResponseEntity<Order> getOrderById(@PathVariable(value = "id") Long orderId)
			throws ResourceNotFoundException {
		Order order = repository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + orderId));
		return ResponseEntity.ok().body(order);
	}

	@GetMapping("customer/{id}")
	@Cacheable(value = "id", key = "customerid")
	public @ResponseBody List<String> getOrderByCustomer(@PathVariable(value = "id") Long customerId)
			throws ResourceNotFoundException {
		List<String> result = service.getOrderList(customerId);
		return result;
	}

	@PostMapping
	public Order createOrder(@RequestBody Order order) {
		// Order Order = service.createObject(OrderRequest);
		order.setId(sequenceGeneratorService.generateSequence(Order.SEQUENCE_NAME));
		service.saveItems(order);
		return repository.save(order);
	}

	@PutMapping("{id}")
	@CachePut(value = "name", key = "id")
	public ResponseEntity<Order> updateOrder(@PathVariable(value = "id") Long orderId,
			@Valid @RequestBody Order orderDetails) throws ResourceNotFoundException {
		Order order = repository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + orderId));
		service.updateItems(orderDetails, order);
		order.setCustomerid(orderDetails.getCustomerid());
		// order.setItems(items);

		final Order updatedOrder = repository.save(order);
		return ResponseEntity.ok(updatedOrder);
	}

	@DeleteMapping("{id}")
	@CacheEvict(value = "name", key = "id")
	public Map<String, Boolean> deleteOrder(@PathVariable(value = "id") Long OrderId) throws ResourceNotFoundException {
		Order Order = repository.findById(OrderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + OrderId));

		repository.delete(Order);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@DeleteMapping("customer/{id}")
	public Map<String, Boolean> deleteCustomer(@PathVariable(value = "id") Long customerId)
			throws ResourceNotFoundException {
		// Order Order = repository.findById(OrderId).orElseThrow(() -> new
		// ResourceNotFoundException("Order not found for this id :: " + OrderId));

		// repository.delete(Order);
		service.delByCustomerId(customerId);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@DeleteMapping("customer")
	public Map<String, Boolean> deleteCustomer() throws ResourceNotFoundException {
		service.delByCustomerId();

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

}
