package com.app.documentmanagement.rabbitmq.service.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.dto.DocumentDTO;
import com.app.documentmanagement.services.AuthorService;
import com.app.documentmanagement.services.DocumentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageConsumerService {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AuthorService authorService;
    
    @RabbitListener(queues = {"${spring.rabbitmq.author.queue.name}"})
    public void consumeAuthorMessage(AuthorDTO authorDTO) {
        log.info(String.format("Author Message Received for Author Id: %s", authorDTO.getId()));
        try {
            authorDTO.getDocuments().forEach(document -> {
                log.info(String.format("Deleting document with ID %s attached to author", authorDTO.getId()));
                documentService.deleteDocumentById(document.getId());
            });

            boolean isDeleted = authorService.deleteAuthorById(authorDTO.getId());
            if(isDeleted) {
                log.info(String.format("Author with ID %s deleted successfully", authorDTO.getId()));
            } else {
                log.info(String.format("Unable to delete Author with ID %s", authorDTO.getId()));
            }
        } catch (Exception exception) {
            log.error("Exception thrown in Author consumer = "+exception.getMessage());
        }
    }

    @RabbitListener(queues = {"${spring.rabbitmq.document.queue.name}"})
    public void consumeDocumentMessage(DocumentDTO documentDTO) {
        log.info(String.format("Document Message Received for document Id: %s", documentDTO.getId()));
        try {
            boolean isDeleted = documentService.deleteDocumentById(documentDTO.getId());
            if(isDeleted) {
                log.info(String.format("Document with ID %s deleted successfully", documentDTO.getId()));
            } else {
                log.info(String.format("Unable to delete Document with ID %s", documentDTO.getId()));
            }    
        } catch (Exception exception) {
            log.error(String.format("Exception thrown in Document consumer = %s"), exception.getMessage());
        }
    }
}
