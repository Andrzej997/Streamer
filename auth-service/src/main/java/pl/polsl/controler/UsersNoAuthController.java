package pl.polsl.controler;

import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.polsl.annotation.AuthMethod;
import pl.polsl.dto.RegistrationDTO;
import pl.polsl.security.Tokenizer;
import pl.polsl.security.service.SecurityService;
import pl.polsl.service.UsersService;

/**
 * Created by Mateusz on 25.10.2016.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/noauth")
public class UsersNoAuthController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private Tokenizer tokenizer;

    public UsersNoAuthController() {

    }

    @AuthMethod(params = {"username", "password"})
    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean>
    login(@RequestParam(value = "username") String username,
          @RequestParam(value = "password") String password) {

        Boolean exists = usersService.userExists(username, password);
        return new ResponseEntity<Boolean>(exists, HttpStatus.OK);
    }

    @AuthMethod(params = {"email", "password"})
    @RequestMapping(value = "/login_by_email", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean>
    loginWithEmail(@RequestParam(value = "email") String email,
                   @RequestParam(value = "password") String password) {

        Boolean userExistsByEmail = usersService.userExistsByEmail(email, password);
        return new ResponseEntity<Boolean>(userExistsByEmail, HttpStatus.OK);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<String>
    registerUser(@RequestBody RegistrationDTO registrationDTO) {

        Boolean success = usersService.registerUser(registrationDTO.getUsername(), registrationDTO.getPassword(), registrationDTO.getEmail());
        if (success) {
            return new ResponseEntity<String>(JSONParser.quote(
                    this.generateToken(registrationDTO.getUsername(), registrationDTO.getPassword(),
                            registrationDTO.getEmail())), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean>
    usernameExists(@RequestParam(value = "username") String username) {

        Boolean exists = usersService.usernameExists(username);
        return new ResponseEntity<Boolean>(exists, HttpStatus.OK);
    }

    @GetMapping(value = "/email/exists", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean>
    checkEmailExists(@RequestParam(value = "email") String email) {

        Boolean exists = usersService.checkEmailExists(email);
        return new ResponseEntity<Boolean>(exists, HttpStatus.OK);
    }

    @GetMapping(value = "/token/verify/username")
    public
    @ResponseBody
    ResponseEntity<Boolean>
    checkUsernameMatchesToken(@RequestParam(value = "token") String token,
                              @RequestParam(value = "username") String username) {

        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(username)) {
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        }
        String usernameFromToken = tokenizer.getUsernameFromToken(token);
        boolean equals = usernameFromToken.equals(username);
        return new ResponseEntity<Boolean>(equals, HttpStatus.OK);
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

    public SecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    public void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }
}

