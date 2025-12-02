package com.example.githubapache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.example.githubapache.service.DataProcessingService;

@SpringBootApplication
public class GitHubApacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitHubApacheApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(DataProcessingService dataProcessingService) {
		return args -> {	
			dataProcessingService.processApacheRepositories();			
		};
	}
}
