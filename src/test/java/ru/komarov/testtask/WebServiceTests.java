package ru.komarov.testtask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebServiceTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final static String BASEURL = "http://localhost:";

    @Test
    public void testDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject(BASEURL + port + "/",
                String.class)).contains("index");
    }

    @Test
    public void testAddAndGetUser() throws Exception {
        URI upsertUserUrl = new URI(BASEURL + port + "/" +
                "user?firstname=alex&lastname=komarov&email=mail&phone=777");
        RequestEntity<?> reqEntity = RequestEntity.put(upsertUserUrl).build();
        ResponseEntity<String> respEntity = this.restTemplate.exchange(reqEntity, String.class);
        assertThat(respEntity.getStatusCode().toString()).contains("201");

        URI getUserUrl = new URI(BASEURL + port + "/" +
                "user?id=1");
        RequestEntity<?> reqEntity2 = RequestEntity.get(getUserUrl).build();
        ResponseEntity<String> respEntity2 = this.restTemplate.exchange(reqEntity2, String.class);
        assertThat(respEntity2.getStatusCode().toString()).contains("200");
    }

    @Test
    public void changeUserStatusTest() throws Exception {
        URI upsertUserUrl = new URI(BASEURL + port + "/" +
                "user?firstname=alex&lastname=komarov&email=mail&phone=777");
        RequestEntity<?> reqEntity = RequestEntity.put(upsertUserUrl).build();
        ResponseEntity<String> respEntity = this.restTemplate.exchange(reqEntity, String.class);
        assertThat(respEntity.getStatusCode().toString()).contains("201");

        URI changeStatusUrl = new URI(BASEURL + port + "/" +
                "user/status?id=1&status=online");
        RequestEntity<?> reqEntity2 = RequestEntity.put(changeStatusUrl).build();
        ResponseEntity<String> respEntity2 = this.restTemplate.exchange(reqEntity2, String.class);
        assertThat(respEntity2.getStatusCode().toString()).contains("200");
    }
}
