package com.levik.search.dto;

import javax.validation.constraints.NotBlank;

public class DocumentRequest {

    @NotBlank
    private String key;

    @NotBlank
    private String tokens;

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
