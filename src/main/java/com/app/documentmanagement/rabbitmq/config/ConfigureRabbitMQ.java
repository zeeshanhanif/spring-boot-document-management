package com.app.documentmanagement.rabbitmq.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ConfigureRabbitMQ {
    
    @Value("${spring.rabbitmq.author.queue.name}")
    private String authorQueue;

    @Value("${spring.rabbitmq.document.queue.name}")
    private String documentQueue;

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchange;

    @Value("${spring.rabbitmq.author.routing.key}")
    private String authorRoutingKey;

    @Value("${spring.rabbitmq.document.routing.key}")
    private String documentRoutingKey;

    @Bean
    Queue createAuthorQueue() {
        return new Queue(authorQueue, false);
    }

    @Bean
    Queue createDocumentQueue() {
        return new Queue(documentQueue, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    Binding authorBinding() {
        return BindingBuilder.bind(createAuthorQueue()).to(exchange()).with(authorRoutingKey);
    }

    @Bean
    Binding documentBinding() {
        return BindingBuilder.bind(createDocumentQueue()).to(exchange()).with(documentRoutingKey);
    }

    /**
     * Setup connection for Producer and Consumer both
     * 
     * @param connectionFactory
     * @return {@Code SimpleMessageListenerContainer}
     */
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        return container;
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
