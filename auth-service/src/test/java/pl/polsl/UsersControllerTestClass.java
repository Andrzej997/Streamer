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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.polsl.controler.UsersControler;
import pl.polsl.service.UsersService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mateusz on 04.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.profiles.active=test", "bootstrap.yml"})
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
