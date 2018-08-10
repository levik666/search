package com.levik.search.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levik.search.AbstractUrlTest;
import com.levik.search.dto.DocumentRequest;
import com.levik.search.dto.SearchResponse;
import com.levik.search.service.StorageService;
import org.junit.After;
import org.junit.Before;
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

import java.util.Arrays;
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
public class SearchApplicationTest extends AbstractUrlTest {

    private String key;
    private String tokens;

    @Autowired
    private StorageService<String, String> storageService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        key =  "setUp" + UUID.randomUUID().toString();
        tokens = "test";

        uploadDocument(key,tokens);
    }

    @After
    public void after() {
        storageService.clear();
    }

    @Test
    public void searchWithoutTokensShouldGetHttpStatus200AndEmptyDocumentKey() throws Exception {
        //given
        String tokens = null;

        SearchResponse<Object> searchResponse = new SearchResponse<>(new LinkedHashSet<Object>(Collections.EMPTY_SET));
        String searchResponseJson = objectMapper.writeValueAsString(searchResponse);

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().path(API).path(SEARCH).queryParam(TOKENS_PARAM, tokens).build();
        ResultActions searchResponseResultActions = this.mockMvc.perform(
                get(uriDocumentComponents.toUri()).accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        //then
        searchResponseResultActions.andExpect(status().isOk());
        searchResponseResultActions.andExpect(content().string(containsString(searchResponseJson)));
    }

    @Test
    public void searchWithCorrectTokensShouldGetHttpStatus200AndExistingDocumentKey() throws Exception {
        //given
        String tokens = this.tokens;

        SearchResponse<String> searchResponse = new SearchResponse<>(new LinkedHashSet<>(Collections.singleton(key)));
        String searchResponseJson = objectMapper.writeValueAsString(searchResponse);

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().path(API).path(SEARCH).queryParam(TOKENS_PARAM, tokens).build();
        ResultActions searchResponseResultActions = this.mockMvc.perform(
                get(uriDocumentComponents.toUri()).accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        //then
        searchResponseResultActions.andExpect(status().isOk());
        searchResponseResultActions.andExpect(content().string(containsString(searchResponseJson)));
    }

    @Test
    public void searchWithWrongTokensShouldGetHttpStatus200AndEmptyDocumentKey() throws Exception {
        //given
        String tokens = "1";

        SearchResponse searchResponse = new SearchResponse<>(Collections.EMPTY_SET);
        String searchResponseJson = objectMapper.writeValueAsString(searchResponse);

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().path(API).path(SEARCH).queryParam(TOKENS_PARAM, tokens).build();
        ResultActions searchResponseResultActions = this.mockMvc.perform(
                get(uriDocumentComponents.toUri()).accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        //then
        searchResponseResultActions.andExpect(status().isOk());
        searchResponseResultActions.andExpect(content().string(containsString(searchResponseJson)));
    }

    @Test
    public void searchWithTokensFromDocumentKeyOneShouldGetHttpStatus200AndDocumentKeyOne() throws Exception {
        //given
        String tokens = "rrr";
        String keySecond = UUID.randomUUID().toString();
        String tokensSecond = "rrr ttt";

        uploadDocument(keySecond, tokensSecond);

        SearchResponse<String> searchResponse = new SearchResponse<>(new LinkedHashSet<>(Collections.singleton(keySecond)));
        String searchResponseJson = objectMapper.writeValueAsString(searchResponse);

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().path(API).path(SEARCH).queryParam(TOKENS_PARAM, tokens).build();
        ResultActions searchResponseResultActions = this.mockMvc.perform(
                get(uriDocumentComponents.toUri()).accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        //then
        searchResponseResultActions.andExpect(status().isOk());
        searchResponseResultActions.andExpect(content().string(containsString(searchResponseJson)));
    }

    @Test
    public void searchWithTokensFromExistingDocumentKeyShouldGetHttpStatus200AndDocumentKeyThreeAndFour() throws Exception {
        //given
        String tokens = "www";
        String keySecond = UUID.randomUUID().toString();
        String tokensSecond = "qqq ttt";

        String keyThree = UUID.randomUUID().toString();
        String tokensThree = "www qqq";

        String keyFour = UUID.randomUUID().toString();
        String tokensFour = "www qqq";

        uploadDocument(keySecond, tokensSecond);
        uploadDocument(keyThree, tokensThree);
        uploadDocument(keyFour, tokensFour);

        SearchResponse<String> searchResponse = new SearchResponse<>(new LinkedHashSet<>(Arrays.asList(keyThree, keyFour)));
        String searchResponseJson = objectMapper.writeValueAsString(searchResponse);

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().path(API).path(SEARCH).queryParam(TOKENS_PARAM, tokens).build();
        ResultActions searchResponseResultActions = this.mockMvc.perform(
                get(uriDocumentComponents.toUri()).accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        //then
        searchResponseResultActions.andExpect(status().isOk());
        searchResponseResultActions.andExpect(content().string(containsString(searchResponseJson)));
    }

    private void uploadDocument(String key, String tokens) throws Exception {
        DocumentRequest request = documentRequest(key, tokens);
        String documentRequestJson = objectMapper.writeValueAsString(request);

        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().path(API).path(DOCUMENT).build();
        this.mockMvc.perform(
                post(uriDocumentComponents.toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(documentRequestJson)
        ).andDo(print()).andExpect(status().isCreated());
    }
}
