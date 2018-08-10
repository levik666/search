package com.levik.search.service.impl;

import com.levik.search.service.StorageService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.Collections.EMPTY_SET;

@Service
@Primary
public class InMemoryStorageParallelComputationService implements StorageService<String, String> {

    private final Map<String, Set<String>> storage;
    private final ExecutorService executorService;

    public InMemoryStorageParallelComputationService(Map<String, Set<String>> storage, ExecutorService executorService) {
        this.storage = storage;
        this.executorService = executorService;
    }

    @Override
    public void put(String key, Set<String> tokens) {
        storage.put(key, tokens);
    }

    @Override
    public Set<String> getByKey(String key) {
        return storage.getOrDefault(key, EMPTY_SET);
    }

    @Override
    public Set<String> getDocumentsKeysThatEqualsByTokens(Set<String> tokens) {
        Set<String> keys = new HashSet<>();
        List<CompletableFuture<Optional<String>>> completableFutures = new ArrayList<>();

        storage.forEach((key, value) -> {
            CompletableFuture<Optional<String>> completableFuture = CompletableFuture.supplyAsync(
                    new SearchWorker(key, value, tokens), executorService
            );
            completableFutures.add(completableFuture);
        });


        completableFutures.forEach(it-> it.join().ifPresent(keys::add));

        return keys;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @PreDestroy
    public void shutdown(){
        executorService.shutdown();
    }
}
