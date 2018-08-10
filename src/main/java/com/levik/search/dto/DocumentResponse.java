package com.levik.search.dto;

import java.util.Objects;
import java.util.Set;

public class DocumentResponse<V> {

    private String key;

    private Set<V> tokens;

    private DocumentResponse() {
    }

    public DocumentResponse(String key, Set<V> tokens) {
        this.key = key;
        this.tokens = tokens;
    }

    public String getKey() {
        return key;
    }

    public Set<V> getTokens() {
        return tokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentResponse<?> response = (DocumentResponse<?>) o;
        return Objects.equals(key, response.key) &&
                Objects.equals(tokens, response.tokens);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key, tokens);
    }
}
