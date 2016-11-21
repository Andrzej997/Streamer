package pl.polsl.controler;

import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.polsl.annotation.AuthMethod;
import pl.polsl.dto.RegistrationDTO;
import pl.polsl.dto.UsersDTO;
import pl.polsl.security.Tokenizer;
import pl.polsl.security.service.SecurityService;
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

    @Autowired
    private SecurityService securityService;

    @Autowired
    private Tokenizer tokenizer;

    public UsersControler() {

    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @AuthMethod(params = {"username", "password"})
    @RequestMapping(value = "/noauth/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean> login(@RequestParam(value = "username") String username,
                                  @RequestParam(value = "password") String password) {
        Boolean exists = usersService.userExists(username, password);
        return new ResponseEntity<Boolean>(exists, HttpStatus.OK);
    }

    @AuthMethod(params = {"email", "password"})
    @RequestMapping(value = "/noauth/login_by_email", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean> loginWithEmail(@RequestParam(value = "email") String email,
                                           @RequestParam(value = "password") String password) {

        Boolean userExistsByEmail = usersService.userExistsByEmail(email, password);
        return new ResponseEntity<Boolean>(userExistsByEmail, HttpStatus.OK);
    }

    @RequestMapping(value = "/noauth/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<String> registerUser(@RequestBody RegistrationDTO registrationDTO) {

        Boolean success = usersService.registerUser(registrationDTO.getUsername(), registrationDTO.getPassword(), registrationDTO.getEmail());
        if (success) {
            return new ResponseEntity<String>(JSONParser.quote(
                    this.generateToken(registrationDTO.getUsername(), registrationDTO.getPassword(),
                            registrationDTO.getEmail())), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/noauth/username", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean> usernameExists(@RequestParam(value = "username") String username) {
        Boolean exists = usersService.usernameExists(username);
        return new ResponseEntity<Boolean>(exists, HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/user_data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PutMapping(value = "/auth/update/user_data", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean> updateUserData(@RequestBody UsersDTO dto) {
        Boolean success = usersService.updateUserInformations(dto) != null;
        return new ResponseEntity<Boolean>(success, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasProtectedAccess()")
    @DeleteMapping(value = "/auth/delete/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean> deleteUser(@RequestBody UsersDTO dto) {
        Boolean deleted = usersService.deleteUser(dto);
        return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
    }

    @GetMapping(value = "/noauth/email/exists", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean> checkEmailExists(@RequestParam(value = "email") String email) {
        Boolean exists = usersService.checkEmailExists(email);
        return new ResponseEntity<Boolean>(exists, HttpStatus.OK);
    }

    @GetMapping(value = "/auth/valid/password", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean> validateOldPassword(@RequestParam(value = "username") String username,
                                                @RequestParam(value = "password") String password) {
        Boolean exists = usersService.userExists(username, password);
        return new ResponseEntity<Boolean>(exists, HttpStatus.OK);
    }

    @PostMapping(value = "/auth/password/change", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean> changePassword(@RequestParam(value = "username") String username,
                                           @RequestParam(value = "password") String password) {
        Boolean success = usersService.changePassword(username, password);
        return new ResponseEntity<Boolean>(success, HttpStatus.OK);
    }

    @GetMapping(value = "/auth/authorize", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean> authorize() {
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    private String generateToken(String username, String password, String email) {
        return this.tokenizer.generateToken(getUserDetails(username, email, password));
    }

    private UserDetails getUserDetails(String username, String email, String password) {
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            return securityService.getUserByUsername(username);
        }
        if (!StringUtils.isEmpty(email) && !StringUtils.isEmpty(password)) {
            return securityService.getUserByEmail(email, password);
        }
        return null;
    }

    public UsersService getUsersService() {
        return usersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }


}

