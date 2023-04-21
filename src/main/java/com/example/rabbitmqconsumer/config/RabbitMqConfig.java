package com.example.rabbitmqconsumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {
    private final AppProperties properties;

    public RabbitMqConfig(AppProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Exchange directExchange(){
        return new DirectExchange(properties.getDirectExchange(), true, false);
    }

    @Bean
    public Queue requestQueue(){
        return QueueBuilder
                .durable(properties.getDirectQueue())
                .withArgument("x-dead-letter-exchange", properties.getDirectQueue())
                .withArgument("x-dead-letter-routing-key", properties.getDirectDeadLetterQueue())
                .build();
    }

    @Bean
    public Binding bindRequestQueue(){
        return BindingBuilder
                .bind(requestQueue())
                .to(directExchange())
                .with(properties.getDirectQueue())
                .noargs();
    }

    @Bean
    public Queue requestDLQueue(){
        return QueueBuilder
                .durable(properties.getDirectDeadLetterQueue())
                .build();
    }

    @Bean
    public Binding bindRequestDLQueue(){
        return BindingBuilder
                .bind(requestDLQueue())
                .to(directExchange())
                .with(properties.getDirectDeadLetterQueue())
                .noargs();
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter(ObjectMapper mapper){
        var converter = new Jackson2JsonMessageConverter(mapper);
        converter.setCreateMessageIds(true); //create a unique message id for every message
        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            ObjectMapper objectMapper) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter(objectMapper));
//        factory.setAcknowledgeMode(AcknowledgeMode.AUTO); //by default set to AUTO
        return factory;
    }
}
