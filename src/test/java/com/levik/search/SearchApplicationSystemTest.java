package com.levik.search;

import com.levik.search.dto.HealthResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SearchApplicationSystemTest extends AbstractTest {

    @Test
    public void actuatorHealthShouldReturnUp() {
        //given
        HealthResponse healthResponse = new HealthResponse();
        healthResponse.setStatus("UP");

        //when
        UriComponents uriHealthComponents = UriComponentsBuilder.newInstance().scheme(SCHEMA).host(HOST).port(port).path(HEALTH).build();
        HealthResponse response = this.restTemplate.getForObject(uriHealthComponents.toUri(), HealthResponse.class);

        //then
        assertThat(response).isEqualTo(healthResponse);
    }
}
