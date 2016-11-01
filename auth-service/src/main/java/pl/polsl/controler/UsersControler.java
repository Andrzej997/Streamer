package pl.polsl.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.polsl.service.UsersService;

import java.util.logging.Logger;

/**
 * Created by Mateusz on 25.10.2016.
 */
@SuppressWarnings("ALL")
@RestController
public class UsersControler {
    protected Logger logger = Logger.getLogger(UsersControler.class.getName());

    @Autowired
    private UsersService usersService;

    public UsersControler() {

    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/login/{username}/{password}", method = RequestMethod.GET)
    public Boolean login(@PathVariable(value = "username") String username,
                         @PathVariable(value = "password") String password) {
        return usersService.userExists(username, password);
    }

    @RequestMapping(value = "/login")
    public String loginWithEmail(@RequestParam(value = "email") String email,
                                 @RequestParam(value = "password") String password) {

        return usersService.getUserData(email);
    }

    public UsersService getUsersService() {
        return usersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }
}

