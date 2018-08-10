package com.levik.search.system;

import com.levik.search.AbstractTest;
import com.levik.search.dto.DocumentRequest;
import com.levik.search.dto.DocumentResponse;
import com.levik.search.dto.SearchResponse;
import com.levik.search.service.StorageService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchSystemTest extends AbstractTest {

    private String key;
    private String tokens;

    @Autowired
    private StorageService<String, String> storageService;

    @Before
    public void setUp() {
        key =  "setUp" + UUID.randomUUID().toString();
        tokens = "test";

        uploadDocument(key,tokens);
    }

    @After
    public void after() {
        storageService.clear();
    }

    @Test
    public void searchWithoutTokensShouldGetHttpStatus200AndEmptyDocumentKey() {
        //given
        String tokens = null;

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port)
                .path(API).path(SEARCH).queryParam(TOKENS_PARAM, tokens).build();
        ResponseEntity<SearchResponse> documentResponseEntity = this.restTemplate.getForEntity(uriDocumentComponents.toUri(), SearchResponse.class);

        //then
        assertThat(documentResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(documentResponseEntity.getBody().getKeys()).isEmpty();
    }

    @Test
    public void searchWithCorrectTokensShouldGetHttpStatus200AndExistingDocumentKey() {
        //given
        String tokens = this.tokens;

        SearchResponse searchResponse = new SearchResponse<>(new LinkedHashSet<>(Collections.singleton(key)));

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port)
                .path(API).path(SEARCH).queryParam(TOKENS_PARAM, tokens).build();
        ResponseEntity<SearchResponse> searchResponseEntity = this.restTemplate.getForEntity(uriDocumentComponents.toUri(), SearchResponse.class);

        //then
        assertThat(searchResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(searchResponseEntity.getBody()).isEqualToComparingFieldByFieldRecursively(searchResponse);
    }

    @Test
    public void searchWithWrongTokensShouldGetHttpStatus200AndEmptyDocumentKey() {
        //given
        String tokens = "1";

        SearchResponse searchResponse = new SearchResponse(Collections.EMPTY_SET);

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port)
                .path(API).path(SEARCH).queryParam(TOKENS_PARAM, tokens).build();
        ResponseEntity<SearchResponse> searchResponseEntity = this.restTemplate.getForEntity(uriDocumentComponents.toUri(), SearchResponse.class);

        //then
        assertThat(searchResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(searchResponseEntity.getBody()).isEqualToComparingFieldByFieldRecursively(searchResponse);
    }

    @Test
    public void searchWithTokensFromDocumentKeyOneShouldGetHttpStatus200AndDocumentKeyOne() {
        //given
        String keySecond = UUID.randomUUID().toString();
        String tokensSecond = "rrr ttt";

        uploadDocument(keySecond, tokensSecond);

        SearchResponse searchResponse = new SearchResponse<>(new LinkedHashSet<>(Collections.singleton(keySecond)));

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port)
                .path(API).path(SEARCH).queryParam(TOKENS_PARAM, "rrr").build();
        ResponseEntity<SearchResponse> searchResponseEntity = this.restTemplate.getForEntity(uriDocumentComponents.toUri(), SearchResponse.class);

        //then
        assertThat(searchResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(searchResponseEntity.getBody()).isEqualToComparingFieldByFieldRecursively(searchResponse);
    }

    @Test
    public void searchWithTokensFromExistingDocumentKeyShouldGetHttpStatus200AndDocumentKeyThreeAndFour() {
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

        SearchResponse searchResponse = new SearchResponse<>(new LinkedHashSet<>(Arrays.asList(keyThree, keyFour)));

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port)
                .path(API).path(SEARCH).queryParam(TOKENS_PARAM, tokens).build();
        ResponseEntity<SearchResponse> searchResponseEntity = this.restTemplate.getForEntity(uriDocumentComponents.toUri(), SearchResponse.class);

        //then
        assertThat(searchResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(searchResponseEntity.getBody().getKeys().size()).isEqualTo(searchResponse.getKeys().size());

        assertThat(searchResponseEntity.getBody().getKeys()).isEqualTo(searchResponse.getKeys());
    }

    private void uploadDocument(String key, String tokens) {
        DocumentRequest request = documentRequest(key, tokens);

        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port)
                .path(API).path(DOCUMENT).build();
        ResponseEntity<DocumentResponse> documentResponseEntity = this.restTemplate.postForEntity(uriDocumentComponents.toUri(), request, DocumentResponse.class);
        assertThat(documentResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    }
}
