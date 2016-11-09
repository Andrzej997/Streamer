package pl.polsl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.polsl.dto.UsersDTO;
import pl.polsl.encryption.ShaEncrypter;
import pl.polsl.mapper.UsersMapper;
import pl.polsl.model.Users;
import pl.polsl.repository.UsersRepository;
import pl.polsl.service.UsersService;
import pl.polsl.service.impl.UsersServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mateusz on 05.11.2016.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"bootstrap.yml"})
public class UsersServiceTestClass {

    @InjectMocks
    private UsersService usersService = new UsersServiceImpl();

    @Mock
    private UsersRepository usersRepository;

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
        Mockito.when(usersRepository.findByUserNameAndPassword("username", ShaEncrypter.sha256("password"))).thenReturn(users);

        Boolean exists = usersService.userExists("username", "password");

        assertThat(exists).isTrue();
    }

    @Test
    public void testUserNotExists() {
        Mockito.when(usersRepository.findByUserNameAndPassword("username", ShaEncrypter.sha256("password"))).thenReturn(null);

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
        Mockito.when(usersRepository.findByEmailAndPassword("email", ShaEncrypter.sha256("password"))).thenReturn(users);

        Boolean exists = usersService.userExistsByEmail("email", "password");

        assertThat(exists).isTrue();
    }

    @Test
    public void testUserNotExists_ByEmail() {
        Mockito.when(usersRepository.findByEmailAndPassword("email", ShaEncrypter.sha256("password"))).thenReturn(null);

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
        Mockito.when(usersRepository.findByUserName("username")).thenReturn(users);
        UsersDTO usersDTO = usersMapper.toUsersDTO(users);

        UsersDTO dto = usersService.getUserData("username");

        assertThat(dto).isEqualToComparingFieldByField(usersDTO);
    }

    @Test
    public void testGetUserData_userNotExists() {
        Mockito.when(usersRepository.findByUserName("username")).thenReturn(null);

        UsersDTO dto = usersService.getUserData("username");

        assertThat(dto).isNull();
    }

    @Test
    public void testRegisterUser_userNotExists() {
        Users user = new Users();
        user.setEmail("email");
        user.setPassword(ShaEncrypter.sha256("password"));
        user.setUserName("username");
        Mockito.when(usersRepository.findByUserName("username")).thenReturn(null);
        Mockito.when(usersRepository.save(user)).thenReturn(user);

        Boolean success = usersService.registerUser("username", "password", "email");

        assertThat(success).isTrue();
    }

    @Test
    public void testRegisterUser_userExists() {
        Users user = new Users();
        user.setEmail("email");
        user.setPassword("password");
        user.setUserName("username");
        Mockito.when(usersRepository.findByUserName("username")).thenReturn(user);

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
        Mockito.when(usersRepository.findOne(1L)).thenReturn(expectedUser);
        Mockito.when(usersRepository.save(expectedUser)).thenReturn(expectedUser);

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
        Mockito.when(usersRepository.findOne(1L)).thenReturn(null);

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
        Mockito.when(usersRepository.findOne(1L)).thenReturn(users);

        Boolean result = usersService.deleteUser(dto);

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
        Mockito.when(usersRepository.findOne(1L)).thenReturn(null);

        Boolean result = usersService.deleteUser(dto);

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
}
