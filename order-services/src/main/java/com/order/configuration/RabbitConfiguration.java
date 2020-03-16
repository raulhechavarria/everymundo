package com.order.configuration;


import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
	
	 private static final String LISTENER_METHOD = "receiveMessage";

	public static final String DEFAULT_PARSING_QUEUE = "receiveMessage";
	
	@Value("${fanout.exchange}")
	private String fanoutExchange;
	@Value("${queue.name}")
	private String queueName;

	@Bean
	Queue queue() {
		return new Queue(queueName, true);
	}

	@Bean
	FanoutExchange exchange() {
		return new FanoutExchange(fanoutExchange);
	}

	@Bean
	org.springframework.amqp.core.Binding binding(Queue queue, FanoutExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange);
	}
}