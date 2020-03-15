package com.customer.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.customer.controller.request.CustomerRequest;
import com.customer.exception.ResourceNotFoundException;
import com.customer.model.Customer;
import com.customer.model.dto.CustomerDTO;


public interface CustomerService {

	List<Customer> find();

	Customer findByID(Long id) throws ResourceNotFoundException;

	Customer save(CustomerRequest objRequest);

	Boolean delete(Long id);

	Customer createObject(CustomerRequest customerRequest);

	void delete(Customer customer);

	CustomerDTO getOrder(Customer customer);


}
