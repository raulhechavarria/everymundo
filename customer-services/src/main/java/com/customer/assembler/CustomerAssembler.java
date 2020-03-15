package com.customer.assembler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.customer.controller.request.CustomerRequest;
import com.customer.model.Customer;
import com.customer.model.dto.CustomerDTO;

public class CustomerAssembler {

	public static Customer customerRequest(CustomerRequest objRequest) {
		Customer customer = new Customer();
		customer.setId(objRequest.getId());
		customer.setLastName(objRequest.getLastName());
		customer.setName(objRequest.getName());	
		customer.setEmail(objRequest.getEmail());
		customer.setDob(objRequest.getDob());
		customer.setActive(objRequest.isActive());
		return customer;
	}
	
	public static CustomerDTO customerRequest(Customer obj) {
		CustomerDTO customerDTO = new CustomerDTO();
		return customerDTO;
	}

	public static CustomerDTO customer(Customer customer, Object[] list) {
		CustomerDTO dto = new CustomerDTO();
				dto.setEmail(customer.getEmail());
				dto.setId(customer.getId());
				dto.setLastName(customer.getLastName());
				dto.setName(customer.getName());
				
				List<String> o = new ArrayList<>();
				for (int i = 0; i < list.length; i++) {
					o.add(list[i].toString());
				}
				dto.setOrders(o);
				
				
		return dto;
	}
	
	
}
