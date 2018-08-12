package com.levik.search.service.impl;

import com.levik.search.dto.CollectionResponse;
import com.levik.search.dto.DocumentRequest;
import com.levik.search.dto.DocumentResponse;
import com.levik.search.dto.SearchResponse;
import com.levik.search.service.SearchService;
import com.levik.search.service.StorageService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.EMPTY_SET;

@Service
public class InMemorySearchEngine implements SearchService {

    private final StorageService<String, String> storage;

    private static final String SPACE_PREFIX = " ";

    public InMemorySearchEngine(StorageService<String, String> storage) {
        this.storage = storage;
    }

    @Override
    public DocumentResponse uploadDocument(DocumentRequest request) {
        Set<String> words = collect(request.getTokens());
        storage.put(request.getKey(), words);
        return new DocumentResponse<>(request.getKey(), words);
    }

    @Override
    public CollectionResponse getTokensByKey(String key) {
        Set<String> tokens = storage.getByKey(key);
        return new CollectionResponse<>(tokens);
    }

    @Override
    public SearchResponse getDocumentKeysByTokens(String tokens) {
        if (tokens != null && !tokens.isEmpty()) {
            Set<String> inputWords = collect(tokens);
            return new SearchResponse<>(storage.getDocumentsKeysThatEqualsByTokens(inputWords));
        }
        return new SearchResponse<String>(EMPTY_SET);
    }

    private Set<String> collect(String q) {
        return Stream.of(q.split(SPACE_PREFIX)).collect(Collectors.toSet());
    }
}
