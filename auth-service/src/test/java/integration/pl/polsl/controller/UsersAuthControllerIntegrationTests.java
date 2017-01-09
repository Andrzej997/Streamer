package integration.pl.polsl.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import pl.polsl.AuthServiceApplication;
import pl.polsl.dto.ChangePasswordDTO;
import pl.polsl.dto.UsersDTO;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mateusz on 05.01.2017.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"bootstrap.yml"}, classes = {AuthServiceApplication.class})
public class UsersAuthControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private String controllerUrl = "/auth";

    private String token = null;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Before
    public void login() {
        String username = "test";
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
        String url = controllerUrl + "/logout";
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
    public void testGetUserData_whenUsernameNotExists() {
        String username = "test5";
        String url = controllerUrl + "/user_data?username={username}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("username", username);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<UsersDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, UsersDTO.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetUserData_whenUsernameExists() {
        String username = "test";
        String url = controllerUrl + "/user_data?username={username}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("username", username);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<UsersDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, UsersDTO.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getEmail()).isEqualTo("test@test");
    }

    @Test
    public void testUpdateUserData_whenUserDTOisEmpty() {
        UsersDTO usersDTO = new UsersDTO();
        String stringUrl = controllerUrl + "/update/user_data";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<UsersDTO> entity = new HttpEntity<>(usersDTO, headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(stringUrl, HttpMethod.PUT, entity, Boolean.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testUpdateUserData_whenUserIdNotExists() {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setUserId(100L);
        String stringUrl = controllerUrl + "/update/user_data";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<UsersDTO> entity = new HttpEntity<>(usersDTO, headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(stringUrl, HttpMethod.PUT, entity, Boolean.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testUpdateUserData_whenUserExists() {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setUserId(1L);
        usersDTO.setUserName("test");
        usersDTO.setEmail("test@test");
        usersDTO.setNationality("EN");
        usersDTO.setName("test");
        usersDTO.setSurname("test");
        String stringUrl = controllerUrl + "/update/user_data";
        HttpHeaders headers = new HttpHeaders();
        headers.add(tokenHeader, token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UsersDTO> entity = new HttpEntity<>(usersDTO, headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(stringUrl, HttpMethod.PUT, entity, Boolean.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isTrue();
    }

    @Test
    public void testValidateOldPassword_whenUsernameNotExists() {
        String username = "test5";
        String password = "test5";
        String url = controllerUrl + "/valid/password?username={username}&password={password}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("username", username);
        urlVariables.put("password", password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Boolean.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testValidateOldPassword_whenPasswordNotMatch() {
        String username = "test";
        String password = "notMatch";
        String url = controllerUrl + "/valid/password?username={username}&password={password}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("username", username);
        urlVariables.put("password", password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Boolean.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testValidateOldPassword_whenPasswordMatch() {
        String username = "test";
        String password = "test";
        String url = controllerUrl + "/valid/password?username={username}&password={password}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("username", username);
        urlVariables.put("password", password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Boolean.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isTrue();
    }

    @Test
    public void testChangePassword_whenUsernameNotExists() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setUsername("test5");
        changePasswordDTO.setOldPassword("test5");
        changePasswordDTO.setNewPassword("test5");
        String url = controllerUrl + "/password/change";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<ChangePasswordDTO> entity = new HttpEntity<>(changePasswordDTO, headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Boolean.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testChangePassword_whenOldPasswordNotMatches() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setUsername("test");
        changePasswordDTO.setOldPassword(null);
        changePasswordDTO.setNewPassword("test");
        String url = controllerUrl + "/password/change";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<ChangePasswordDTO> entity = new HttpEntity<>(changePasswordDTO, headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Boolean.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testChangePassword_whenNewPasswordIsNull() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setUsername("test");
        changePasswordDTO.setOldPassword("test");
        changePasswordDTO.setNewPassword(null);
        String url = controllerUrl + "/password/change";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<ChangePasswordDTO> entity = new HttpEntity<>(changePasswordDTO, headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Boolean.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void shouldChangePassword() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setUsername("test2");
        changePasswordDTO.setOldPassword("test");
        changePasswordDTO.setNewPassword("test4");
        String url = controllerUrl + "/password/change";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<ChangePasswordDTO> entity = new HttpEntity<>(changePasswordDTO, headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Boolean.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isTrue();
    }

    @Test
    public void testAuthorize_afterLogout() {
        String url = controllerUrl + "/authorize";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        logout();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("T");
    }

    @Test
    public void testAuthorize() {
        String url = controllerUrl + "/authorize";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(tokenHeader, token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo("T");
    }
}
