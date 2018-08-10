package com.levik.search.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levik.search.AbstractUrlTest;
import com.levik.search.dto.CollectionResponse;
import com.levik.search.dto.DocumentRequest;
import com.levik.search.dto.DocumentResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DocumentUploadApplicationTest extends AbstractUrlTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void documentUploadShouldReturnHttpStatus201WithPayload() throws Exception {
        //given
        String key = UUID.randomUUID().toString();
        String tokens = "tests";

        DocumentRequest request = documentRequest(key, tokens);
        DocumentResponse response = new DocumentResponse<>(key, new LinkedHashSet<>(Collections.singleton(tokens)));
        String documentRequestJson = objectMapper.writeValueAsString(request);
        String documentResponseJson = objectMapper.writeValueAsString(response);

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().path(API).path(DOCUMENT).build();
        ResultActions documentUploadResultActions = this.mockMvc.perform(
                post(uriDocumentComponents.toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(documentRequestJson)
        ).andDo(print());

        //then
        documentUploadResultActions.andExpect(status().isCreated());
        documentUploadResultActions.andExpect(content().string(containsString(documentResponseJson)));
    }

    @Test
    public void getDocumentByKeyShouldReturnDocumentWithExistingKey() throws Exception {
        //given
        String key = UUID.randomUUID().toString();
        String tokens = "tests";

        DocumentRequest request = documentRequest(key, tokens);
        CollectionResponse collectionResponse = new CollectionResponse<>(new LinkedHashSet<>(Collections.singleton(tokens)));

        String documentRequestJson = objectMapper.writeValueAsString(request);
        String collectionRequestJson = objectMapper.writeValueAsString(collectionResponse);

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().path(API).path(DOCUMENT).build();
        this.mockMvc.perform(
                post(uriDocumentComponents.toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(documentRequestJson)
        ).andDo(print()).andExpect(status().isCreated());

        UriComponents uriDocumentByKeyComponents = UriComponentsBuilder.newInstance().path(API).path(DOCUMENT).path("/" + key).build();
        ResultActions getDocumentByKeyResultActions = this.mockMvc.perform(
                get(uriDocumentByKeyComponents.toUri()).accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        //then
        getDocumentByKeyResultActions.andExpect(status().isOk());
        getDocumentByKeyResultActions.andExpect(content().string(containsString(collectionRequestJson)));
    }
}
