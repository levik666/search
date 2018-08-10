package com.levik.search.service;

import java.util.Set;

public interface StorageService<K, T> {

    void put(K key, Set<T> tokens);

    Set<T> getByKey(K key);

    Set<K> getDocumentsKeysThatEqualsByTokens(Set<T> tokens);

    void clear();
}
