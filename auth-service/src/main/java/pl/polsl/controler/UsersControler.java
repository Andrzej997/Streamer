package pl.polsl.controler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.encryption.ShaEncrypter;
import pl.polsl.model.Users;
import pl.polsl.repository.UsersRepository;

import java.util.logging.Logger;

/**
 * Created by Mateusz on 25.10.2016.
 */
@RestController
public class UsersControler {
    protected Logger logger = Logger.getLogger(UsersControler.class.getName());

    @Autowired
    private UsersRepository usersRepository;

    public UsersControler() {

    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/login/{username}/{password}", method = RequestMethod.GET)
    public Boolean login(@PathVariable(value = "username") String username,
                         @PathVariable(value = "password") String password) {
        Users user = usersRepository.findByUserName(username);
        if (user == null || StringUtils.isEmpty(password)) {
            return false;
        }
        String sha256 = ShaEncrypter.sha256(password);
        return sha256.equals(user.getPassword());
    }

}

