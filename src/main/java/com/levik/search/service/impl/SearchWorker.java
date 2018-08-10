package com.levik.search.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class SearchWorker implements Supplier<Optional<String>> {

    private final String key;
    private final Set<String> values;
    private final Set<String> tokens;

    public SearchWorker(String key, Set<String> values, Set<String> tokens) {
        this.key = key;
        this.values = values;
        this.tokens = tokens;
    }

    @Override
    public Optional<String> get() {
        Optional<String> key = Optional.empty();
        if (values.containsAll(tokens)) {
            return Optional.of(this.key);
        }
        return key;
    }
}
