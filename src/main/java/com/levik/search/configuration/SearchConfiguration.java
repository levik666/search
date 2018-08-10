package com.levik.search.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class SearchConfiguration {

    @Bean
    public Map<String, Set<String>>  storage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.setName("parallelComputation " + UUID.randomUUID().toString());
            return thread;
        });
    }
}
