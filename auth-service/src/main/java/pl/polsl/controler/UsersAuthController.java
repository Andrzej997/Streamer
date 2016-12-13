package pl.polsl.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.polsl.dto.ChangePasswordDTO;
import pl.polsl.dto.UsersDTO;
import pl.polsl.service.UsersService;

import java.util.List;

/**
 * Created by Mateusz on 07.12.2016.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/auth")
public class UsersAuthController {

    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/user_data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<UsersDTO>
    getUserData(@RequestParam(value = "username") String username) {

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
    ResponseEntity<Boolean>
    updateUserData(@RequestBody UsersDTO dto) {

        Boolean success = usersService.updateUserInformations(dto) != null;
        return new ResponseEntity<Boolean>(success, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasProtectedAccess()")
    @DeleteMapping(value = "/admin/delete/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean>
    deleteUser(@RequestParam("id") Long userId) {

        Boolean deleted = usersService.deleteUser(userId);
        return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
    }

    @GetMapping(value = "/valid/password", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean>
    validateOldPassword(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password) {

        Boolean exists = usersService.userExists(username, password);
        return new ResponseEntity<Boolean>(exists, HttpStatus.OK);
    }

    @PostMapping(value = "/password/change", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Boolean>
    changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {

        Boolean success = usersService.changePassword(changePasswordDTO);
        return new ResponseEntity<Boolean>(success, HttpStatus.OK);
    }

    @GetMapping(value = "/authorize", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<String>
    authorize() {
        return new ResponseEntity<String>("T", HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasProtectedAccess()")
    @GetMapping("/admin")
    public
    @ResponseBody
    ResponseEntity<Boolean> isAdmin() {
        return ResponseEntity.ok(true);
    }

    @PreAuthorize("@securityService.hasProtectedAccess()")
    @GetMapping("/admin/users")
    public
    @ResponseBody
    ResponseEntity<List<UsersDTO>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }


    public UsersService getUsersService() {
        return usersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

}
