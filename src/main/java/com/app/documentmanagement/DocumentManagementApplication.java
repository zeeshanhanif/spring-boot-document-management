package com.app.documentmanagement;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DocumentManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentManagementApplication.class, args);
	}

	@Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

}
