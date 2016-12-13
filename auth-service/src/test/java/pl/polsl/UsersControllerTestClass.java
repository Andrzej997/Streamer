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
import pl.polsl.controler.UsersAuthController;
import pl.polsl.controler.UsersNoAuthController;
import pl.polsl.dto.ChangePasswordDTO;
import pl.polsl.dto.RegistrationDTO;
import pl.polsl.dto.UsersDTO;
import pl.polsl.model.Users;
import pl.polsl.security.Tokenizer;
import pl.polsl.security.model.SecuredUser;
import pl.polsl.security.service.SecurityService;
import pl.polsl.service.UsersService;

import java.util.ArrayList;

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
    private UsersNoAuthController usersNoAuthController;

    @InjectMocks
    private UsersAuthController usersAuthController;

    @Mock
    private UsersService usersService;

    @Mock
    private Tokenizer tokenizer;

    @Mock
    private SecurityService securityService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLogin_whenUserExist() {
        when(usersService.userExists("username", "password")).thenReturn(true);

        ResponseEntity<Boolean> login = usersNoAuthController.login("username", "password");

        assertThat(login).isNotNull();
        assertThat(login.getBody()).isTrue();
    }

    @Test
    public void testLogin_whenUserNotExists() {
        when(usersService.userExists("username", "password")).thenReturn(false);

        ResponseEntity<Boolean> login = usersNoAuthController.login("username", "password");

        assertThat(login).isNotNull();
        assertThat(login.getBody()).isFalse();
    }

    @Test
    public void testLoginWithEmail_whenUserExists() {
        when(usersService.userExistsByEmail("email", "password")).thenReturn(true);

        ResponseEntity<Boolean> login = usersNoAuthController.loginWithEmail("email", "password");

        assertThat(login).isNotNull();
        assertThat(login.getBody()).isTrue();
    }

    @Test
    public void testLoginWithEmail_whenNotUserExists() {
        when(usersService.userExistsByEmail("email", "password")).thenReturn(false);

        ResponseEntity<Boolean> login = usersNoAuthController.loginWithEmail("email", "password");

        assertThat(login).isNotNull();
        assertThat(login.getBody()).isFalse();
    }

    @Test
    public void testRegisterUser_whenUserNotExists() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setEmail("email");
        registrationDTO.setPassword("password");
        registrationDTO.setUsername("username");
        SecuredUser securedUser = new SecuredUser();
        securedUser.setId(1L);
        securedUser.setPassword("password");
        securedUser.setEmail("email");
        securedUser.setUsername("username");
        securedUser.setAuthorities(new ArrayList<>());
        securedUser.setAccountNonExpired(true);
        securedUser.setAccountNonLocked(true);
        securedUser.setCredentialsNonExpired(true);
        securedUser.setEnabled(true);

        when(usersService.registerUser("username", "password", "email")).thenReturn(true);
        when(securityService.getUserByUsername(registrationDTO.getUsername())).thenReturn(securedUser);
        when(tokenizer.generateToken(securedUser)).thenReturn("DDDD");

        ResponseEntity<String> result = usersNoAuthController.registerUser(registrationDTO);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotEmpty();
    }

    @Test
    public void testRegisterUser_whenUserExists() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setEmail("email");
        registrationDTO.setPassword("password");
        registrationDTO.setUsername("username");
        when(usersService.registerUser("username", "password", "email")).thenReturn(false);

        ResponseEntity<String> result = usersNoAuthController.registerUser(registrationDTO);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNull();
    }

    @Test
    public void testGetUserData_whenUserExists() {
        UsersDTO dto = new UsersDTO();
        when(usersService.getUserData("username")).thenReturn(dto);

        ResponseEntity<UsersDTO> result = usersAuthController.getUserData("username");

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(dto);
    }

    @Test
    public void testGetUserData_whenUserNotExists() {
        when(usersService.getUserData("username")).thenReturn(null);

        ResponseEntity<UsersDTO> result = usersAuthController.getUserData("username");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdateUserData_whenUserExists() {
        UsersDTO dto = new UsersDTO();
        when(usersService.updateUserInformations(dto)).thenReturn(new Users());

        ResponseEntity<Boolean> result = usersAuthController.updateUserData(dto);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    @Test
    public void testUpdateUserData_whenUserNotExists() {
        UsersDTO dto = new UsersDTO();
        when(usersService.updateUserInformations(dto)).thenReturn(null);

        ResponseEntity<Boolean> result = usersAuthController.updateUserData(dto);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
    }

    @Test
    public void testDeleteUser_whenUserExists() {
        Long userId = 1L;
        when(usersService.deleteUser(userId)).thenReturn(true);

        ResponseEntity<Boolean> result = usersAuthController.deleteUser(userId);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    @Test
    public void testDeleteUser_whenUserNotExists() {
        Long userId = 1L;
        when(usersService.deleteUser(userId)).thenReturn(false);

        ResponseEntity<Boolean> result = usersAuthController.deleteUser(userId);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
    }

    @Test
    public void testUsernameExists_whenNotExists() {
        when(usersService.usernameExists("username")).thenReturn(false);

        ResponseEntity<Boolean> result = usersNoAuthController.usernameExists("username");

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
    }

    @Test
    public void testUsernameExists_whenExists() {
        when(usersService.usernameExists("username")).thenReturn(true);

        ResponseEntity<Boolean> result = usersNoAuthController.usernameExists("username");

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    @Test
    public void testEmailExists_whenExists() {
        String email = "email";
        when(usersService.checkEmailExists(email)).thenReturn(true);

        ResponseEntity<Boolean> result = usersNoAuthController.checkEmailExists(email);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    @Test
    public void testEmailExists_whenNotExists() {
        String email = "email";
        when(usersService.checkEmailExists(email)).thenReturn(false);

        ResponseEntity<Boolean> result = usersNoAuthController.checkEmailExists(email);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
    }

    @Test
    public void testChangePassword_whenUserExists() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setNewPassword("newPassword");
        changePasswordDTO.setOldPassword("oldPassword");
        changePasswordDTO.setUsername("username");

        when(usersService.changePassword(changePasswordDTO)).thenReturn(true);

        ResponseEntity<Boolean> result = usersAuthController.changePassword(changePasswordDTO);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    @Test
    public void testChangePassword_whenUserNotExists() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setNewPassword("newPassword");
        changePasswordDTO.setOldPassword("oldPassword");
        changePasswordDTO.setUsername("username");

        when(usersService.changePassword(changePasswordDTO)).thenReturn(false);

        ResponseEntity<Boolean> result = usersAuthController.changePassword(changePasswordDTO);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
    }

    public void testValidateOldPassword_whenUserExists() {
        String username = "username";
        String password = "password";
        when(usersService.userExists(username, password)).thenReturn(true);

        ResponseEntity<Boolean> result = usersAuthController.validateOldPassword(username, password);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isTrue();
    }

    public void testValidateOldPassword_whenUserNotExists() {
        String username = "username";
        String password = "password";
        when(usersService.userExists(username, password)).thenReturn(false);

        ResponseEntity<Boolean> result = usersAuthController.validateOldPassword(username, password);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isFalse();
    }

    public UsersNoAuthController getUsersNoAuthController() {
        return usersNoAuthController;
    }

    public void setUsersNoAuthController(UsersNoAuthController usersNoAuthController) {
        this.usersNoAuthController = usersNoAuthController;
    }

    public UsersService getUsersService() {
        return usersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

}
