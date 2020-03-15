package com.customer.controller;

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

import com.customer.controller.request.CustomerRequest;
import com.customer.exception.ResourceNotFoundException;
import com.customer.model.Customer;
import com.customer.model.dto.CustomerDTO;
import com.customer.repository.CustomerRepository;
import com.customer.service.CustomerService;
import com.customer.service.impl.SequenceGeneratorService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	
	@Autowired
	private CustomerRepository repository;
	
	@Autowired
	CustomerService service;
	
	
	@GetMapping
	@Cacheable(value = "name", key = "id")
	public List<Customer> getAllCustomers() {
		return repository.findAll();
	}

	@GetMapping("{id}")
	@Cacheable(value = "name", key = "id")
	public @ResponseBody CustomerDTO getCustomerById(@PathVariable(value = "id") Long customerId) throws ResourceNotFoundException{
		Customer customer = service.findByID(customerId);
		return service.getOrder(customer);
	}

	@PostMapping
	public Customer createCustomer(@RequestBody CustomerRequest customer) {
		return service.save(customer);
	}

	@PutMapping("{id}")
	@CachePut(value = "name", key = "id")
	public ResponseEntity<Customer> updateCustomer(@PathVariable(value = "id") Long customerId,
			@Valid @RequestBody CustomerRequest customerDetails) throws ResourceNotFoundException {
		
		customerDetails.setId(customerId);		
		final Customer updatedCustomer = service.save(customerDetails);
		return ResponseEntity.ok(updatedCustomer);
	}

	@DeleteMapping("{id}")
	@CacheEvict(value = "name", key = "id")
	public Map<String, Boolean> deleteCustomer(@PathVariable(value = "id") Long customerId)
			throws ResourceNotFoundException {
		Map<String, Boolean> response = new HashMap<>();
		if (service.delete(customerId)) {
			response.put("deleted", Boolean.TRUE);
		} else {
			response.put("not deleted", Boolean.FALSE);
		}
		
		return response;
	}

}
