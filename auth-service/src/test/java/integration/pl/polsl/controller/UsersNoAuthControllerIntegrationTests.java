package integration.pl.polsl.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.polsl.AuthServiceApplication;
import pl.polsl.dto.RegistrationDTO;

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
public class UsersNoAuthControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private String controllerUrl = "/noauth";

    @Test
    public void testLogin_whenUserExists() {
        String username = "test";
        String password = "test";
        String url = controllerUrl + "/login?username={username}&password={password}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("username", username);
        urlVariables.put("password", password);

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(url, String.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    public void testLogin_whenUserNotExists() {
        String username = "notExists";
        String password = "notExists";
        String url = controllerUrl + "/login?username={username}&password={password}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("username", username);
        urlVariables.put("password", password);

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(url, String.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testLogin_whenPasswordIsNotCorrect() {
        String username = "test";
        String password = "notCorrect";
        String url = controllerUrl + "/login?username={username}&password={password}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("username", username);
        urlVariables.put("password", password);

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(url, String.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testLoginWithEmail_whenEmailExists() {
        String email = "test@test";
        String password = "test";
        String url = controllerUrl + "/login_by_email?email={email}&password={password}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("email", email);
        urlVariables.put("password", password);

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(url, String.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    public void testLoginWithEmail_whenEmailNotExists() {
        String email = "notExists@test";
        String password = "test";
        String url = controllerUrl + "/login_by_email?email={email}&password={password}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("email", email);
        urlVariables.put("password", password);

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(url, String.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testLoginWithEmail_whenPasswordIsBad() {
        String email = "test@test";
        String password = "badPassword";
        String url = controllerUrl + "/login_by_email?email={email}&password={password}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("email", email);
        urlVariables.put("password", password);

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(url, String.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testUsernameExists_whenUsernameExists() {
        String username = "test";
        String url = controllerUrl + "/username?username={username}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("username", username);

        ResponseEntity<Boolean> responseEntity =
                restTemplate.getForEntity(url, Boolean.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isTrue();
    }

    @Test
    public void testUsernameExists_whenUsernameNotExists() {
        String username = "notExists";
        String url = controllerUrl + "/username?username={username}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("username", username);

        ResponseEntity<Boolean> responseEntity =
                restTemplate.getForEntity(url, Boolean.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testRegisterUser_whenUsernameIsNull() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setPassword("password");
        registrationDTO.setEmail("email");
        String url = controllerUrl + "/register";

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, registrationDTO, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testRegisterUser_whenPasswordIsNull() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("username");
        registrationDTO.setEmail("email");
        String url = controllerUrl + "/register";

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, registrationDTO, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testRegisterUser_whenEmailIsNull() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("username");
        registrationDTO.setPassword("password");
        String url = controllerUrl + "/register";

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, registrationDTO, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testRegisterUser_whenUsernameIsNotUnique() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("test");
        registrationDTO.setPassword("password");
        registrationDTO.setEmail("test5@test5");
        String url = controllerUrl + "/register";

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, registrationDTO, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testRegisterUser_whenEmailIsNotUnique() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("test5");
        registrationDTO.setPassword("password");
        registrationDTO.setEmail("test@test");
        String url = controllerUrl + "/register";

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, registrationDTO, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void shouldSuccessRegisterUser() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("test5");
        registrationDTO.setPassword("password");
        registrationDTO.setEmail("test5@test5");
        String url = controllerUrl + "/register";

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, registrationDTO, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().length()).isGreaterThan(1);
    }

    @Test
    public void testCheckEmailExists_whenEmailNotExists() {
        String email = "email@email";
        String url = controllerUrl + "/email/exists?email={email}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("email", email);

        ResponseEntity<Boolean> responseEntity =
                restTemplate.getForEntity(url, Boolean.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testCheckEmailExists_whenEmailExists() {
        String email = "test@test";
        String url = controllerUrl + "/email/exists?email={email}";
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("email", email);

        ResponseEntity<Boolean> responseEntity =
                restTemplate.getForEntity(url, Boolean.class, urlVariables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isTrue();
    }
}
