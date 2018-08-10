package com.levik.search.system;

import com.levik.search.AbstractTest;
import com.levik.search.dto.CollectionResponse;
import com.levik.search.dto.DocumentRequest;
import com.levik.search.dto.DocumentResponse;
import com.levik.search.dto.HealthResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DocumentUploadSystemTest extends AbstractTest {

    @Test
    public void documentUploadShouldReturnHttpStatus201WithPayload() {
        //given
        String key = UUID.randomUUID().toString();
        String tokens = "tests";

        DocumentRequest request = documentRequest(key, tokens);
        DocumentResponse response = new DocumentResponse<>(key, new LinkedHashSet<>(Collections.singleton(tokens)));

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port).path(API).path(DOCUMENT).build();
        ResponseEntity<DocumentResponse> documentResponseEntity = this.restTemplate.postForEntity(uriDocumentComponents.toUri(), request, DocumentResponse.class);

        //then
        assertThat(documentResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        assertThat(documentResponseEntity.getBody()).isEqualToComparingFieldByFieldRecursively(response);
    }

    @Test
    public void getDocumentByKeyShouldReturnDocumentWithExistingKey() {
        //given
        String key = UUID.randomUUID().toString();
        String tokens = "tests";

        DocumentRequest request = documentRequest(key, tokens);

        CollectionResponse collectionResponse = new CollectionResponse<>(new LinkedHashSet<>(Collections.singleton(tokens)));

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port).path(API).path(DOCUMENT).build();
        ResponseEntity<DocumentResponse> documentResponseEntity = this.restTemplate.postForEntity(uriDocumentComponents.toUri(), request, DocumentResponse.class);
        assertThat(documentResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);

        UriComponents uriDocumentByKeyComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port).path(API).path(DOCUMENT).path("/" + key).build();
        ResponseEntity<CollectionResponse> collectionResponseEntity = this.restTemplate.getForEntity(uriDocumentByKeyComponents.toUri(), CollectionResponse.class);

        //then
        assertThat(collectionResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(collectionResponseEntity.getBody()).isEqualToComparingFieldByFieldRecursively(collectionResponse);
    }
}
