package com.app.documentmanagement;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.app.documentmanagement.repositories.DocumentRepository;
import com.app.documentmanagement.services.DocumentService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * {@DocumentManagementApplication} class is used to bootstrap and launch a Spring application
 * from a Java main method. 
 * This class automatically creates the ApplicationContext from the classpath, scan the 
 * configuration classes and launch the application
 * 
 * @author Zeeshan Hanif
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Document Management API", version = "1.0", description = "Doucment Management for Authors"))
public class DocumentManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentManagementApplication.class, args);
	}

	@Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

}
