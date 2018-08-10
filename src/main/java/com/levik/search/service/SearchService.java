package com.levik.search.service;

import com.levik.search.dto.CollectionResponse;
import com.levik.search.dto.DocumentRequest;
import com.levik.search.dto.DocumentResponse;
import com.levik.search.dto.SearchResponse;


public interface SearchService {

    DocumentResponse uploadDocument(DocumentRequest request);

    CollectionResponse getTokensByKey(String key);

    SearchResponse getDocumentKeysByTokens(String tokens);
}
