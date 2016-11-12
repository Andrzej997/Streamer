package pl.polsl.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.dto.RegistrationDTO;
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
    public
    @ResponseBody
    Boolean login(@RequestParam(value = "username") String username,
                  @RequestParam(value = "password") String password) {
        return usersService.userExists(username, password);
    }

    @RequestMapping(value = "/login_by_email", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Boolean loginWithEmail(@RequestParam(value = "email") String email,
                           @RequestParam(value = "password") String password) {

        return usersService.userExistsByEmail(email, password);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Boolean registerUser(@RequestBody RegistrationDTO registrationDTO) {
        return usersService.registerUser(registrationDTO.getUsername(), registrationDTO.getPassword(), registrationDTO.getEmail());
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Boolean usernameExists(@RequestParam(value = "username") String username) {
        return usersService.usernameExists(username);
    }

    @RequestMapping(value = "/user_data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<UsersDTO> getUserData(@RequestParam(value = "username") String username) {
        UsersDTO user = usersService.getUserData(username);
        if (user != null) {
            return new ResponseEntity<UsersDTO>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<UsersDTO>(new UsersDTO(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/user_data", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Boolean updateUserData(@RequestBody UsersDTO dto) {
        return usersService.updateUserInformations(dto) != null;
    }

    @DeleteMapping(value = "/delete/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Boolean deleteUser(@RequestBody UsersDTO dto) {
        return usersService.deleteUser(dto);
    }

    @GetMapping(value = "/email/exists", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Boolean checkEmailExists(@RequestParam(value = "email") String email) {
        return usersService.checkEmailExists(email);
    }

    public UsersService getUsersService() {
        return usersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }


}

