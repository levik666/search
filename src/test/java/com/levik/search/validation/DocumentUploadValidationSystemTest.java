package com.levik.search.validation;

import com.levik.search.AbstractTest;
import com.levik.search.dto.DocumentRequest;
import com.levik.search.dto.DocumentResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DocumentUploadValidationSystemTest extends AbstractTest {

    @Test
    public void documentUploadShouldReturnHttpStatus400WhenKeyIsBlank() {
        //given
        String key = null;
        String tokens = "tests";

        DocumentRequest request = documentRequest(key, tokens);

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port).path(API).path(DOCUMENT).build();
        ResponseEntity<DocumentResponse> documentResponseEntity = this.restTemplate.postForEntity(uriDocumentComponents.toUri(), request, DocumentResponse.class);

        //then
        assertThat(documentResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void documentUploadShouldReturnHttpStatus400WhenTokensIsBlank() {
        //given
        String key = UUID.randomUUID().toString();
        String tokens = null;

        DocumentRequest request = documentRequest(key, tokens);

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port).path(API).path(DOCUMENT).build();
        ResponseEntity<DocumentResponse> documentResponseEntity = this.restTemplate.postForEntity(uriDocumentComponents.toUri(), request, DocumentResponse.class);

        //then
        assertThat(documentResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void documentUploadShouldReturnHttpStatus400WhenAllFieldsIsBlank() {
        //given
        String key = null;
        String tokens = null;

        DocumentRequest request = documentRequest(key, tokens);

        //when
        UriComponents uriDocumentComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port).path(API).path(DOCUMENT).build();
        ResponseEntity<DocumentResponse> documentResponseEntity = this.restTemplate.postForEntity(uriDocumentComponents.toUri(), request, DocumentResponse.class);

        //then
        assertThat(documentResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }
}
