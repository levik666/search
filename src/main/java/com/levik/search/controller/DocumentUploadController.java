package com.levik.search.controller;

import com.levik.search.dto.CollectionResponse;
import com.levik.search.dto.DocumentRequest;
import com.levik.search.dto.DocumentResponse;
import com.levik.search.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/")
public class DocumentUploadController {

    private final SearchService searchService;

    public DocumentUploadController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/document")
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentResponse uploadDocument(@RequestBody @Valid DocumentRequest request) {
        return searchService.uploadDocument(request);
    }

    @GetMapping("/document/{key}")
    public CollectionResponse getTokensByKey(@PathVariable("key") String key) {
        return searchService.getTokensByKey(key);
    }
}
