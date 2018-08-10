package com.levik.search;

import com.levik.search.dto.DocumentRequest;

public class AbstractUrlTest {

    protected static final String SCHEMA = "http";
    protected static final String HOST = "localhost";
    protected static final String API = "/api/v1";
    protected static final String DOCUMENT = "/document";
    protected static final String SEARCH = "/search";
    protected static final String TOKENS_PARAM = "tokens";
    static final String HEALTH = "/actuator/health";

    protected DocumentRequest documentRequest(String key, String tokens) {
        DocumentRequest request = new DocumentRequest();
        request.setKey(key);
        request.setTokens(tokens);

        return request;
    }
}


