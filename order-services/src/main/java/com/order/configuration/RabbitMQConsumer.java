package com.order.configuration;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.order.model.dto.OrderDTO;
import com.order.service.impl.OrderServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RabbitMQConsumer {
	
	protected Logger logger = (Logger) LoggerFactory.getLogger(getClass());
	@Value("${fanout.exchange}")
	private String fanoutExchange;
	//private final RabbitTemplate rabbitTemplate;
	
	
//	@RabbitListener(queues = "${javainuse.rabbitmq.queue}")
//	public void recievedMessage(Customer customer) {
//		System.out.println("Recieved Message From RabbitMQ: " + customer);
//	}

	public void receiveMessage(String message) {
//		logger.info("Received (String) " + message);
		processMessage(message);
	}

	public void receiveMessage(byte[] message) {
		String strMessage = new String(message);
		logger.info("Received (No String) " + strMessage);
		processMessage(strMessage);
	}

	private void processMessage(String message) {
		try {
			String orderDTO = new ObjectMapper().readValue(message, String.class);
//	   ValidationUtil.validateMailDTO(mailDTO);
		} catch (JsonParseException e) {
			logger.warn("Bad JSON in message: " + message);
		} catch (JsonMappingException e) {
			logger.warn("cannot map JSON to NotificationRequest: " + message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}