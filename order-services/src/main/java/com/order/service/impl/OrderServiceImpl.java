package com.order.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.order.configuration.Customer;
import com.order.configuration.RabbitConfiguration;
import com.order.configuration.RabbitMQConsumer;
import com.order.exception.ResourceNotFoundException;
import com.order.model.Item;
import com.order.model.Order;
import com.order.repository.ItemRepository;
import com.order.repository.OrderRepository;
import com.order.service.OrderService;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class OrderServiceImpl implements OrderService {

	private final MongoTemplate mongoTemplate;

	@Autowired
	RabbitMQConsumer rabbitMQConsumer;

	@Autowired
	RabbitConfiguration rabbitConfiguration;

	@Autowired
	OrderRepository repository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	public OrderServiceImpl(MongoTemplate mongoTemplate) {
		super();
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Order save(Order order) {
		order.setId(sequenceGeneratorService.generateSequence(Order.SEQUENCE_NAME));
		order.getItems().forEach(action -> {
			((Item) action).setId(sequenceGeneratorService.generateSequence(Item.SEQUENCE_NAME));
			itemRepository.save((Item) action);
		});
		return repository.save(order);

	}

	@Override
	public void updateItems(@Valid Order orderDetails, Order order) {

		for (Item i : order.getItems()) {
			itemRepository.delete(i);
		}
		order.setItems(orderDetails.getItems());

	}

	@Override
	public void delByCustomerId(Long customerId) {

		Query query = new Query().addCriteria(Criteria.where("customerid").is(customerId.toString()));

		List<Order> list = mongoTemplate.find(query, Order.class);

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Order order = (Order) iterator.next();

			repository.delete(order);

		}

	}

	@Override
	public List<String> getOrderList(Long customerId) {
		Query query = new Query().addCriteria(Criteria.where("customerid").is(customerId.toString()));

		List<Order> list = mongoTemplate.find(query, Order.class);
		List<String> result = new ArrayList<String>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Order order = (Order) iterator.next();
			for (Item i : order.getItems()) {
				result.add(i.getName() + " , " + i.getPrice());
			}
			// repository.delete(order);

		}
		return result;
	}

	@Override
	public void delByCustomerId() {

		String queue = rabbitConfiguration.getQueueName();
		String string = rabbitConfiguration.getFanoutExchange();
		Queue queue2 = rabbitConfiguration.queue();
		// String queString = queue2.;

		// rabbitMQConsumer.receiveMessage("Deleted message");

		// String strMessage = new String(message);
		// logger.info("Received (No String) " + strMessage);
		// System.out.print(strMessage);
		// objectMessage("", "", new Message(null, null) );
		/*
		 * ConnectionFactory connectionFactory = new CachingConnectionFactory();
		 * AmqpAdmin admin = new RabbitAdmin(connectionFactory); admin.declareQueue(new
		 * Queue("myqueue")); AmqpTemplate template = new
		 * RabbitTemplate(connectionFactory); template.convertAndSend("myqueue", "foo");
		 * String foo = (String) template.receiveAndConvert("myqueue");
		 */

	}

	@Override
	public Order findByID(Long orderId) throws ResourceNotFoundException {
		Order order = repository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + orderId));
		return order;
	}

	@Override
	public Order update(@Valid Order orderDetails) throws ResourceNotFoundException {
		Order order = repository.findById(orderDetails.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + orderDetails.getId()));
		updateItems(orderDetails, order);
		order.setCustomerid(orderDetails.getCustomerid());
		final Order updatedOrder = repository.save(order);

		return updatedOrder;
	}

}
