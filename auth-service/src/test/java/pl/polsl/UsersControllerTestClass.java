package pl.polsl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.polsl.controler.UsersControler;
import pl.polsl.dto.UsersDTO;
import pl.polsl.model.Users;
import pl.polsl.service.UsersService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mateusz on 04.11.2016.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"bootstrap.yml"})
public class UsersControllerTestClass {

    @InjectMocks
    private UsersControler usersControler;

    @Mock
    private UsersService usersService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIndex() {
        String body = this.restTemplate.getForObject("/", String.class);
        assertThat(body).isEqualTo("index");
    }

    @Test
    public void testLogin_whenUserExist() {
        Mockito.when(usersService.userExists("username", "password")).thenReturn(true);

        Boolean login = usersControler.login("username", "password");

        assertThat(login).isTrue();
    }

    @Test
    public void testLogin_whenUserNotExists() {
        Mockito.when(usersService.userExists("username", "password")).thenReturn(false);

        Boolean login = usersControler.login("username", "password");

        assertThat(login).isFalse();
    }

    @Test
    public void testLoginWithEmail_whenUserExists() {
        Mockito.when(usersService.userExistsByEmail("email", "password")).thenReturn(true);

        Boolean login = usersControler.loginWithEmail("email", "password");

        assertThat(login).isTrue();
    }

    @Test
    public void testLoginWithEmail_whenNotUserExists() {
        Mockito.when(usersService.userExistsByEmail("email", "password")).thenReturn(false);

        Boolean login = usersControler.loginWithEmail("email", "password");

        assertThat(login).isFalse();
    }

    @Test
    public void testRegisterUser_whenUserNotExists() {
        Mockito.when(usersService.registerUser("username", "password", "email")).thenReturn(true);

        String result = usersControler.registerUser("username", "password", "email");

        assertThat(result).isEqualTo("Registration succesful");
    }

    @Test
    public void testRegisterUser_whenUserExists() {
        Mockito.when(usersService.registerUser("username", "password", "email")).thenReturn(false);

        String result = usersControler.registerUser("username", "password", "email");

        assertThat(result).isEqualTo("Registration failed");
    }

    @Test
    public void testGetUserData_whenUserExists() {
        UsersDTO dto = new UsersDTO();
        Mockito.when(usersService.getUserData("username")).thenReturn(dto);

        ResponseEntity<UsersDTO> result = usersControler.getUserData("username");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(dto);
    }

    @Test
    public void testGetUserData_whenUserNotExists() {
        Mockito.when(usersService.getUserData("username")).thenReturn(null);

        ResponseEntity<UsersDTO> result = usersControler.getUserData("username");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdateUserData_whenUserExists() {
        UsersDTO dto = new UsersDTO();
        Mockito.when(usersService.updateUserInformations(dto)).thenReturn(new Users());

        Boolean result = usersControler.updateUserData(dto);

        assertThat(result).isTrue();
    }

    @Test
    public void testUpdateUserData_whenUserNotExists() {
        UsersDTO dto = new UsersDTO();
        Mockito.when(usersService.updateUserInformations(dto)).thenReturn(null);

        Boolean result = usersControler.updateUserData(dto);

        assertThat(result).isFalse();
    }

    @Test
    public void testDeleteUser_whenUserExists() {
        UsersDTO dto = new UsersDTO();
        Mockito.when(usersService.deleteUser(dto)).thenReturn(true);

        Boolean result = usersControler.deleteUser(dto);

        assertThat(result).isTrue();
    }

    @Test
    public void testDeleteUser_whenUserNotExists() {
        UsersDTO dto = new UsersDTO();
        Mockito.when(usersService.deleteUser(dto)).thenReturn(false);

        Boolean result = usersControler.deleteUser(dto);

        assertThat(result).isFalse();
    }

    public UsersControler getUsersControler() {
        return usersControler;
    }

    public void setUsersControler(UsersControler usersControler) {
        this.usersControler = usersControler;
    }

    public UsersService getUsersService() {
        return usersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public TestRestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
