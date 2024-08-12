package com.app.documentmanagement.rabbitmq.service.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.dto.DocumentDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageProducerService {
    
    @Value("${spring.rabbitmq.exchange.name}")
    private String exchange;

    @Value("${spring.rabbitmq.author.routing.key}")
    private String authorRoutingKey;

    @Value("${spring.rabbitmq.document.routing.key}")
    private String documentRoutingKey;

    private RabbitTemplate rabbitTemplate;

    public MessageProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAuthorMessage(AuthorDTO authorDTO) {
        try {
            rabbitTemplate.convertAndSend(exchange, authorRoutingKey, authorDTO);
        } catch (Exception e) {
            log.error("Exception in Sending Author message: "+e.getMessage());
        }
        log.info(String.format("Author Message Sent for author with Id: %s", authorDTO.getId()));
    }

    public void sendDocumentMessage(DocumentDTO documentDTO) {
        try {
            rabbitTemplate.convertAndSend(exchange, documentRoutingKey, documentDTO);
        } catch (Exception e) {
            log.error("Exception in Sending Document message: "+e.getMessage());
        }
        log.info(String.format("Document Message Sent for document with Id: %s", documentDTO.getId()));
    }
}
