package integration.pl.polsl.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import pl.polsl.AuthServiceApplication;
import pl.polsl.dto.UsersDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mateusz on 09.01.2017.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"bootstrap.yml"}, classes = {AuthServiceApplication.class})
@AutoConfigureTestDatabase
public class UsersAdminControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private String controllerUrl = "/admin";

    private String token = null;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Before
    public void loginAsAdmin() {
        String username = "admin";
        String password = "test";
        String url = "/noauth/login?username={username}&password={password}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("username", username);
        urlVariables.put("password", password);

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(url, String.class, urlVariables);

        String response = responseEntity.getBody();
        if (!StringUtils.isEmpty(response)) {
            token = response.substring(1, response.length() - 1);
        }
    }

    @After
    public void logout() {
        String url = "/auth/logout";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(url, entity, Boolean.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        token = null;
    }

    @Test
    public void testIsAdmin() {
        String url = controllerUrl + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Boolean.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isTrue();
    }

    @Test
    public void testGetAllUsers() {
        String url = controllerUrl + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        List<UsersDTO> list = new ArrayList<>();

        ResponseEntity<List<UsersDTO>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity,
                (Class<List<UsersDTO>>) list.getClass());

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isGreaterThan(3);
    }

    @Test
    public void testDeleteUser_whenUserNotExists() {
        Long userId = 100L;
        String url = controllerUrl + "/delete/user?id={id}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("id", userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> responseEntity =
                restTemplate.exchange(url, HttpMethod.DELETE, entity, Boolean.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testDeleteUser_whenUserExists() {
        Long userId = 5L;
        String url = controllerUrl + "/delete/user?id={id}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("id", userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> responseEntity =
                restTemplate.exchange(url, HttpMethod.DELETE, entity, Boolean.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isTrue();
    }

}
