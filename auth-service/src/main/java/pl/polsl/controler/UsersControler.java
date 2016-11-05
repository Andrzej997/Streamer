package pl.polsl.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.dto.UsersDTO;
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

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean login(@RequestParam(value = "username") String username,
                         @RequestParam(value = "password") String password) {
        return usersService.userExists(username, password);
    }

    @RequestMapping(value = "/login_by_email", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean loginWithEmail(@RequestParam(value = "email") String email,
                                 @RequestParam(value = "password") String password) {

        return usersService.userExistsByEmail(email, password);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String registerUser(@RequestParam(value = "username") String username,
                               @RequestParam(value = "password") String password,
                               @RequestParam(value = "email") String email) {
        Boolean success = usersService.registerUser(username, password, email);
        if (success) {
            return "Registration succesful";
        } else {
            return "Registration failed";
        }
    }

    @RequestMapping(value = "/user_data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsersDTO> getUserData(String username) {
        UsersDTO user = usersService.getUserData(username);
        return new ResponseEntity<UsersDTO>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/update/user_data", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean updateUserData(@RequestBody UsersDTO dto) {
        return usersService.updateUserInformations(dto) != null;
    }

    @DeleteMapping(value = "/delete/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean deleteUser(@RequestBody UsersDTO dto) {
        return usersService.deleteUser(dto);
    }

    public UsersService getUsersService() {
        return usersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }


}

