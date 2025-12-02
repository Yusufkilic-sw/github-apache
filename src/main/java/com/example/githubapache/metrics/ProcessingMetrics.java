package com.example.githubapache.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProcessingMetrics {

    private final MeterRegistry meterRegistry;

    private Counter repositoriesProcessedCounter;
    private Counter contributorsProcessedCounter;
    private Counter errorCounter;
    private Timer processingTimer;

    public void initialize() {
        repositoriesProcessedCounter = Counter.builder("github.repositories.processed")
                .description("Total number of repositories processed")
                .register(meterRegistry);

        contributorsProcessedCounter = Counter.builder("github.contributors.processed")
                .description("Total number of contributors processed")
                .register(meterRegistry);

        errorCounter = Counter.builder("github.processing.errors")
                .description("Total number of processing errors")
                .register(meterRegistry);

        processingTimer = Timer.builder("github.processing.duration")
                .description("Time taken to process Apache repositories")
                .register(meterRegistry);

        log.info("Metrics initialized successfully");
    }

    @PostConstruct
    private void postConstructInit() {
        if (processingTimer == null) {
            initialize();
        }
    }

    public void incrementRepositoriesProcessed() {
        repositoriesProcessedCounter.increment();
    }

    public void incrementContributorsProcessed() {
        contributorsProcessedCounter.increment();
    }

    public void incrementErrors() {
        errorCounter.increment();
    }

    public Timer.Sample startProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordProcessingTime(Timer.Sample sample) {
        sample.stop(processingTimer);
    }
}
