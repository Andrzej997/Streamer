package unit.pl.polsl.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.polsl.AuthServiceApplication;
import pl.polsl.dto.ChangePasswordDTO;
import pl.polsl.dto.UsersDTO;
import pl.polsl.encryption.ShaEncrypter;
import pl.polsl.mapper.UsersMapper;
import pl.polsl.model.Authority;
import pl.polsl.model.Users;
import pl.polsl.repository.AuthorityRepository;
import pl.polsl.repository.UsersRepository;
import pl.polsl.service.UsersService;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Mateusz on 05.11.2016.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"bootstrap.yml"}, classes = {AuthServiceApplication.class})
public class UsersServiceTestClass {

    @Autowired
    @InjectMocks
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Autowired
    @Spy
    private UsersMapper usersMapper;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUserExists() {
        Users users = new Users();
        users.setUserId(1L);
        users.setUserName("username");
        users.setPassword(ShaEncrypter.sha256("password"));
        users.setEmail("email");
        when(usersRepository.findByUserNameAndPassword("username", ShaEncrypter.sha256("password"))).thenReturn(users);

        Boolean exists = usersService.userExists("username", "password");

        assertThat(exists).isTrue();
    }

    @Test
    public void testUserNotExists() {
        when(usersRepository.findByUserNameAndPassword("username", ShaEncrypter.sha256("password"))).thenReturn(null);

        Boolean exists = usersService.userExists("username", "password");

        assertThat(exists).isFalse();
    }

    @Test
    public void testUserExists_ByEmail() {
        Users users = new Users();
        users.setUserId(1L);
        users.setUserName("username");
        users.setPassword(ShaEncrypter.sha256("password"));
        users.setEmail("email");
        when(usersRepository.findByEmailAndPassword("email", ShaEncrypter.sha256("password"))).thenReturn(users);

        Boolean exists = usersService.userExistsByEmail("email", "password");

        assertThat(exists).isTrue();
    }

    @Test
    public void testUserNotExists_ByEmail() {
        when(usersRepository.findByEmailAndPassword("email", ShaEncrypter.sha256("password"))).thenReturn(null);

        Boolean exists = usersService.userExistsByEmail("email", "password");

        assertThat(exists).isFalse();
    }

    @Test
    public void testGetUserData() {
        Users users = new Users();
        users.setUserId(1L);
        users.setUserName("username");
        users.setPassword(ShaEncrypter.sha256("password"));
        users.setEmail("email");
        when(usersRepository.findByUserName("username")).thenReturn(users);
        UsersDTO usersDTO = usersMapper.toUsersDTO(users);

        UsersDTO dto = usersService.getUserData("username");

        assertThat(dto).isEqualToComparingFieldByField(usersDTO);
    }

    @Test
    public void testGetUserData_userNotExists() {
        when(usersRepository.findByUserName("username")).thenReturn(null);

        UsersDTO dto = usersService.getUserData("username");

        assertThat(dto).isNull();
    }

    @Test
    public void testRegisterUser_userNotExists() {
        Users user = new Users();
        user.setEmail("email");
        user.setPassword(ShaEncrypter.sha256("password"));
        user.setUserName("username");
        user.setAccountLocked(false);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 120);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        user.setPasswordExpirationDate(calendar.getTime());
        user.setAccountExpirationDate(calendar.getTime());
        Authority authority = new Authority();
        authority.setUserId(user.getUserId());
        authority.setAuthority("ROLE_USER");
        when(usersRepository.findByUserName("username")).thenReturn(null);
        when(usersRepository.findByEmail("email")).thenReturn(null);
        when(usersRepository.save(user)).thenReturn(user);
        when(authorityRepository.save(authority)).thenReturn(authority);

        Boolean success = usersService.registerUser("username", "password", "email");

        assertThat(success).isTrue();
    }

    @Test
    public void testRegisterUser_userExists() {
        Users user = new Users();
        user.setEmail("email");
        user.setPassword("password");
        user.setUserName("username");
        when(usersRepository.findByUserName("username")).thenReturn(user);

        Boolean success = usersService.registerUser("username", "password", "email");

        assertThat(success).isFalse();
    }

    @Test
    public void testUpdateUserInformations_userExists() {
        UsersDTO dto = new UsersDTO();
        dto.setUserId(1L);
        dto.setSurname("surname");
        dto.setName("name");
        dto.setUserName("username");
        dto.setEmail("email");
        dto.setNationality("nationality");
        Users expectedUser = usersMapper.toUsers(dto);
        when(usersRepository.findOne(1L)).thenReturn(expectedUser);
        when(usersRepository.save(expectedUser)).thenReturn(expectedUser);

        Users result = usersService.updateUserInformations(dto);

        assertThat(result).isEqualToComparingFieldByField(expectedUser);
    }

    @Test
    public void testUpdateUserInformations_userNotExists() {
        UsersDTO dto = new UsersDTO();
        dto.setUserId(1L);
        dto.setSurname("surname");
        dto.setName("name");
        dto.setUserName("username");
        dto.setEmail("email");
        dto.setNationality("nationality");
        when(usersRepository.findOne(1L)).thenReturn(null);

        Users result = usersService.updateUserInformations(dto);

        assertThat(result).isNull();
    }

    @Test
    public void testUpdateUserInformations_dtoIsNull() {

        Users result = usersService.updateUserInformations(null);

        assertThat(result).isNull();
    }

    @Test
    public void testDeleteUser_userExists() {
        UsersDTO dto = new UsersDTO();
        dto.setUserId(1L);
        dto.setSurname("surname");
        dto.setName("name");
        dto.setUserName("username");
        dto.setEmail("email");
        dto.setNationality("nationality");
        Users users = usersMapper.toUsers(dto);
        when(usersRepository.findOne(1L)).thenReturn(users);
        Mockito.doNothing().when(usersRepository).delete(users);

        Boolean result = usersService.deleteUser(1L);

        assertThat(result).isTrue();
    }

    @Test
    public void testDeleteUser_userNotExists() {
        UsersDTO dto = new UsersDTO();
        dto.setUserId(1L);
        dto.setSurname("surname");
        dto.setName("name");
        dto.setUserName("username");
        dto.setEmail("email");
        dto.setNationality("nationality");
        when(usersRepository.findOne(1L)).thenReturn(null);

        Boolean result = usersService.deleteUser(1L);

        assertThat(result).isFalse();
    }

    @Test
    public void testUsernameExists_usernameNotExists() {
        String username = "username";
        when(usersRepository.countByUserName(username)).thenReturn(0L);

        Boolean result = usersService.usernameExists(username);

        assertThat(result).isFalse();
    }

    @Test
    public void testUsernameExists_usernameExists() {
        String username = "username";
        when(usersRepository.countByUserName(username)).thenReturn(1L);

        Boolean result = usersService.usernameExists(username);

        assertThat(result).isTrue();
    }

    @Test
    public void testEmailExists_emailNotExists() {
        String email = "email";
        when(usersRepository.countByEmail(email)).thenReturn(0L);

        Boolean result = usersService.checkEmailExists(email);

        assertThat(result).isFalse();
    }

    @Test
    public void testEmailExists_emailExists() {
        String email = "email";
        when(usersRepository.countByEmail(email)).thenReturn(1L);

        Boolean result = usersService.checkEmailExists(email);

        assertThat(result).isTrue();
    }

    @Test
    public void testChangePassword_whenUserExists() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setNewPassword("newPassword");
        changePasswordDTO.setOldPassword("oldPassword");
        changePasswordDTO.setUsername("username");
        Users users = new Users();
        users.setUserName(changePasswordDTO.getUsername());
        users.setPassword(changePasswordDTO.getOldPassword());
        users.setEmail("email");
        users.setUserId(1L);

        when(usersRepository.findByUserNameAndPassword(changePasswordDTO.getUsername(),
                ShaEncrypter.sha256(changePasswordDTO.getOldPassword()))).thenReturn(users);
        when(usersRepository.findByUserName(changePasswordDTO.getUsername())).thenReturn(users);
        users.setPassword(ShaEncrypter.sha256(changePasswordDTO.getNewPassword()));
        when(usersRepository.save(users)).thenReturn(users);

        Boolean result = usersService.changePassword(changePasswordDTO);

        assertThat(result).isTrue();
    }

    @Test
    public void testChangePassword_whenUserNotExists() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setNewPassword("newPassword");
        changePasswordDTO.setOldPassword("oldPassword");
        changePasswordDTO.setUsername("username");
        Users users = new Users();
        users.setUserName(changePasswordDTO.getUsername());
        users.setPassword(changePasswordDTO.getOldPassword());
        users.setEmail("email");
        users.setUserId(1L);

        when(usersRepository.findByUserNameAndPassword(changePasswordDTO.getUsername(),
                ShaEncrypter.sha256(changePasswordDTO.getOldPassword()))).thenReturn(null);
        when(usersRepository.findByUserName(changePasswordDTO.getUsername())).thenReturn(users);
        users.setPassword(ShaEncrypter.sha256(changePasswordDTO.getNewPassword()));
        when(usersRepository.save(users)).thenReturn(users);

        Boolean result = usersService.changePassword(changePasswordDTO);

        assertThat(result).isFalse();
    }

    public UsersService getUsersService() {
        return usersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public UsersRepository getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UsersMapper getUsersMapper() {
        return usersMapper;
    }

    public void setUsersMapper(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    public AuthorityRepository getAuthorityRepository() {
        return authorityRepository;
    }

    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }
}
