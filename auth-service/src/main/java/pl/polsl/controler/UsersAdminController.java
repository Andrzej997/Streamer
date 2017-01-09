package pl.polsl.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.polsl.dto.UsersDTO;
import pl.polsl.service.UsersService;

import java.util.List;

/**
 * Created by Mateusz on 09.01.2017.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/admin")
public class UsersAdminController {

    @Autowired
    private UsersService usersService;

    @PreAuthorize("@securityService.hasProtectedAccess()")
    @GetMapping("/")
    public
    @ResponseBody
    ResponseEntity<Boolean> isAdmin() {
        return ResponseEntity.ok(true);
    }

    @PreAuthorize("@securityService.hasProtectedAccess()")
    @GetMapping("/users")
    public
    @ResponseBody
    ResponseEntity<List<UsersDTO>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @PreAuthorize("@securityService.hasProtectedAccess()")
    @DeleteMapping(value = "/delete/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean>
    deleteUser(@RequestParam("id") Long userId) {

        Boolean deleted = usersService.deleteUser(userId);
        return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
    }
}
