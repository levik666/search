package com.levik.search.dto;

import java.util.Objects;
import java.util.Set;

public class SearchResponse<T> {

    private Set<T> keys;

    private SearchResponse() {
    }

    public SearchResponse(Set<T> keys) {
        this.keys = keys;
    }

    public Set<T> getKeys() {
        return keys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResponse<?> that = (SearchResponse<?>) o;
        return Objects.equals(keys, that.keys);
    }

    @Override
    public int hashCode() {

        return Objects.hash(keys);
    }
}
