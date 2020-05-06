package com.customer.configuration;

//import org.apache.logging.log4j.Logger;

import ch.qos.logback.classic.Logger;


import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class QueueProducer {
	protected Logger logger = (Logger) LoggerFactory.getLogger(getClass());
	@Value("${fanout.exchange}")
	private String fanoutExchange;
	private final RabbitTemplate rabbitTemplate;

	@Autowired
	public QueueProducer(RabbitTemplate rabbitTemplate) {
		super();
		this.rabbitTemplate = rabbitTemplate;
	}

	public Object produce(Long long1) throws Exception {
		logger.info("Storing notification...");
		rabbitTemplate.setExchange(fanoutExchange);
		rabbitTemplate.convertAndSend(new ObjectMapper().writeValueAsString(long1));
	//	 MessageConverter tem =  rabbitTemplate.getMessageConverter();
		logger.info("Notification stored in queue sucessfully   heeeeeeeeee");
		return fanoutExchange;
	}
}