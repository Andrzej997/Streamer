package pl.polsl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.polsl.controler.UsersControler;
import pl.polsl.dto.RegistrationDTO;
import pl.polsl.dto.UsersDTO;
import pl.polsl.model.Users;
import pl.polsl.service.UsersService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Mateusz on 04.11.2016.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"bootstrap.yml"})
public class UsersControllerTestClass {

    @InjectMocks
    private UsersControler usersControler;

    @Mock
    private UsersService usersService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLogin_whenUserExist() {
        when(usersService.userExists("username", "password")).thenReturn(true);

        ResponseEntity<Boolean> login = usersControler.login("username", "password");

        assertThat(login).isNotNull();
        assertThat(login.getBody()).isTrue();
    }

    @Test
    public void testLogin_whenUserNotExists() {
        when(usersService.userExists("username", "password")).thenReturn(false);

        ResponseEntity<Boolean> login = usersControler.login("username", "password");

        assertThat(login).isNotNull();
        assertThat(login.getBody()).isFalse();
    }

    @Test
    public void testLoginWithEmail_whenUserExists() {
        when(usersService.userExistsByEmail("email", "password")).thenReturn(true);

        ResponseEntity<Boolean> login = usersControler.loginWithEmail("email", "password");

        assertThat(login).isNotNull();
        assertThat(login.getBody()).isTrue();
    }

    @Test
    public void testLoginWithEmail_whenNotUserExists() {
        when(usersService.userExistsByEmail("email", "password")).thenReturn(false);

        ResponseEntity<Boolean> login = usersControler.loginWithEmail("email", "password");

        assertThat(login).isNotNull();
        assertThat(login.getBody()).isFalse();
    }

    @Test
    public void testRegisterUser_whenUserNotExists() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setEmail("email");
        registrationDTO.setPassword("password");
        registrationDTO.setUsername("username");
        when(usersService.registerUser("username", "password", "email")).thenReturn(true);

        ResponseEntity<Boolean> result = usersControler.registerUser(registrationDTO);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    @Test
    public void testRegisterUser_whenUserExists() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setEmail("email");
        registrationDTO.setPassword("password");
        registrationDTO.setUsername("username");
        when(usersService.registerUser("username", "password", "email")).thenReturn(false);

        ResponseEntity<Boolean> result = usersControler.registerUser(registrationDTO);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
    }

    @Test
    public void testGetUserData_whenUserExists() {
        UsersDTO dto = new UsersDTO();
        when(usersService.getUserData("username")).thenReturn(dto);

        ResponseEntity<UsersDTO> result = usersControler.getUserData("username");

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(dto);
    }

    @Test
    public void testGetUserData_whenUserNotExists() {
        when(usersService.getUserData("username")).thenReturn(null);

        ResponseEntity<UsersDTO> result = usersControler.getUserData("username");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdateUserData_whenUserExists() {
        UsersDTO dto = new UsersDTO();
        when(usersService.updateUserInformations(dto)).thenReturn(new Users());

        ResponseEntity<Boolean> result = usersControler.updateUserData(dto);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    @Test
    public void testUpdateUserData_whenUserNotExists() {
        UsersDTO dto = new UsersDTO();
        when(usersService.updateUserInformations(dto)).thenReturn(null);

        ResponseEntity<Boolean> result = usersControler.updateUserData(dto);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
    }

    @Test
    public void testDeleteUser_whenUserExists() {
        UsersDTO dto = new UsersDTO();
        when(usersService.deleteUser(dto)).thenReturn(true);

        ResponseEntity<Boolean> result = usersControler.deleteUser(dto);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    @Test
    public void testDeleteUser_whenUserNotExists() {
        UsersDTO dto = new UsersDTO();
        when(usersService.deleteUser(dto)).thenReturn(false);

        ResponseEntity<Boolean> result = usersControler.deleteUser(dto);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
    }

    @Test
    public void testUsernameExists_whenNotExists() {
        when(usersService.usernameExists("username")).thenReturn(false);

        ResponseEntity<Boolean> result = usersControler.usernameExists("username");

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
    }

    @Test
    public void testUsernameExists_whenExists() {
        when(usersService.usernameExists("username")).thenReturn(true);

        ResponseEntity<Boolean> result = usersControler.usernameExists("username");

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    @Test
    public void testEmailExists_whenExists() {
        String email = "email";
        when(usersService.checkEmailExists(email)).thenReturn(true);

        ResponseEntity<Boolean> result = usersControler.checkEmailExists(email);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    @Test
    public void testEmailExists_whenNotExists() {
        String email = "email";
        when(usersService.checkEmailExists(email)).thenReturn(false);

        ResponseEntity<Boolean> result = usersControler.checkEmailExists(email);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
    }

    @Test
    public void testChangePassword_whenUserExists() {
        String username = "username";
        String password = "password";
        when(usersService.changePassword(username, password)).thenReturn(true);

        ResponseEntity<Boolean> result = usersControler.changePassword(username, password);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    @Test
    public void testChangePassword_whenUserNotExists() {
        String username = "username";
        String password = "password";
        when(usersService.changePassword(username, password)).thenReturn(false);

        ResponseEntity<Boolean> result = usersControler.changePassword(username, password);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
    }

    public void testValidateOldPassword_whenUserExists() {
        String username = "username";
        String password = "password";
        when(usersService.userExists(username, password)).thenReturn(true);

        ResponseEntity<Boolean> result = usersControler.validateOldPassword(username, password);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    public void testValidateOldPassword_whenUserNotExists() {
        String username = "username";
        String password = "password";
        when(usersService.userExists(username, password)).thenReturn(false);

        ResponseEntity<Boolean> result = usersControler.validateOldPassword(username, password);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
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

}
