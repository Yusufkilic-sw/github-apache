package com.example.githubapache.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.githubapache.metrics.ProcessingMetrics;

@Component
@RequiredArgsConstructor
@Slf4j
public class MetricsInitializer implements CommandLineRunner {

    private final ProcessingMetrics processingMetrics;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing application metrics...");
        processingMetrics.initialize();
        log.info("Metrics initialization completed");
    }
}
