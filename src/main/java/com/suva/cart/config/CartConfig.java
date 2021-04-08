package com.suva.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableMongoRepositories(basePackages = "com.suva.cart.repository")
public class CartConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
