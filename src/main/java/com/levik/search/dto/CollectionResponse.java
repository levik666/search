package com.levik.search.dto;

import java.util.Set;

public class CollectionResponse<T> {

    private Set<T> tokens;

    private CollectionResponse() {
    }

    public CollectionResponse(Set<T> tokens) {
        this.tokens = tokens;
    }

    public Set<T> getTokens() {
        return tokens;
    }
}
