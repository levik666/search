package com.levik.search.controller;

import com.levik.search.dto.SearchResponse;
import com.levik.search.service.SearchService;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/api/v1/search")
    public SearchResponse search(@RequestParam(value = "tokens", required = false) String tokens) {
        return searchService.getDocumentKeysByTokens(tokens);
    }
}
