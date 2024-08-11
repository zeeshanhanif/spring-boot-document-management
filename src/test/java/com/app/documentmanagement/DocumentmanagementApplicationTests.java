package com.app.documentmanagement;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest
@TestConfiguration
class DocumentmanagementApplicationTests {

	@Test
	void contextLoads() {
	}

	@Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
