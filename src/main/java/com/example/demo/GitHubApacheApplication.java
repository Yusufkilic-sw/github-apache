package com.example.demo;

import com.example.demo.service.DataProcessingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GitHubApacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitHubApacheApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(DataProcessingService dataProcessingService) {
		return args -> {
			System.out.println("Starting GitHub Apache ..");
			dataProcessingService.processApacheRepositories();
			System.out.println("Completed - data saved to H2 (in-memory)");
		};
	}
}
