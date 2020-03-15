package com.customer.service.impl;

import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.customer.assembler.CustomerAssembler;
import com.customer.configuration.QueueProducer;
import com.customer.controller.request.CustomerRequest;
import com.customer.exception.ResourceNotFoundException;
import com.customer.model.Customer;
import com.customer.model.dto.CustomerDTO;
import com.customer.repository.CustomerRepository;
import com.customer.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository repository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	
	@Autowired
	QueueProducer queueProducer;
	
	
	@Override
	public List<Customer> find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer findByID(Long id) throws ResourceNotFoundException {
			Customer customer = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + id));
		
		return customer;
	}

	@Override
	public Customer save(CustomerRequest objRequest) {
		Customer customer = new Customer();
		if (    Long.valueOf(objRequest.getId()) == 0) {
			objRequest.setId(sequenceGeneratorService.generateSequence(Customer.SEQUENCE_NAME));
			
		}		
		customer = CustomerAssembler.customerRequest(objRequest);
		repository.save(customer);
		return customer;
	}

	@Override
	public Boolean delete(Long id) {
		
		try {
			Customer customer = repository.findById(id).get();
			repository.deleteById(id);
			return true; 
		} catch (Exception e) {
			return false; 
		}
		
		
	}

	@Override
	public Customer createObject(CustomerRequest customerRequest) {
		Customer customer = new Customer();
		return customer;
	}

	@Override
	public void delete(Customer customer) {
	//	final String uri = "http://localhost:9003/order/customer/" + customer.getId().toString();
	//	RestTemplate restTemplate = new RestTemplate();
	//	restTemplate.delete(uri);
		
		repository.delete(customer);
		
		//sendMessage(customer);
		  try {
			queueProducer.produce(customer.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				// .orElseThrow(() -> new ResourceNotFoundException("Rhp not found"));
		
	}

	private void sendMessage(Customer customer) {
		final String uri = "http://localhost:5672/send/" + customer.getId().toString();
		RestTemplate restTemplate = new RestTemplate();
		
		Object[] list = restTemplate.getForObject(uri, Object[].class);
	}

	@Override
	public CustomerDTO getOrder(Customer customer) {
		final String uri = "http://localhost:9003/order/customer/" + customer.getId().toString();
		RestTemplate restTemplate = new RestTemplate();
		
		Object[] list = restTemplate.getForObject(uri, Object[].class);
		
		CustomerDTO result = CustomerAssembler.customer(customer, list);
		return result;
	}

	
	

}
